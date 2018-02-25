package com.su.blog;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTestsApplication {

    //数据库连接
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	//数据库类型和java类型对比
    @Autowired
    private jdbcTypeProperties jdbcTypeProperties;

    //模板文件名
//    private String[] vmFiles = {".vm","Mapper.vm","xml.vm","Controller.vm"};
    private String[] vmFiles = {".vm","Mapper.vm","Service.vm","Controller.vm","IService.vm","xml.vm"};
    //模板输出对应位置
//    private String[] vmPackageFiles = {"/model/","/mapper/","/mapper/","/controller/"};
    private String[] vmPackageFiles = {"/model/", "/mapper/","/service/impl/","/controller/","/service/", "/mapper/"};
    //模板存储位置
	private String vmFilePath = "/templates/";


	@Test
    public void createVm(){
        try {
            //数据库连接
            Connection conn = sqlSessionFactory.openSession().getConnection();
            createFile("menu","D:/","com.su.blog",false,conn, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * 得到表的表结构
	 * @param tableName 表名
	 * @param isJava 符合java命名规范
	 * @return
	 * @throws SQLException
	 */
	public List<JdbcItem> contextLoads(String tableName,boolean isJava,Connection conn) throws SQLException {
		List<JdbcItem> list = new ArrayList<>();
		System.out.println(jdbcTypeProperties.getId());
		DatabaseMetaData data = conn.getMetaData();
		ResultSet rs = data.getColumns(null, null,  tableName, "%");
		System.out.println("start print tableItem");
		while (rs.next()) {
			//打印字段name信息
			String columnName = rs.getString("COLUMN_NAME");
			if("id".equals(columnName)){
			    continue;
            }
			if(isJava){
				columnName = commitString(columnName);
			}
            String typeName = jdbcTypeProperties.getJdbcTypeToJavaType().get(rs.getString(6));
            String comment = rs.getString(12);
            list.add(new JdbcItem(columnName, columnName.substring(0,1).toUpperCase()+columnName.substring(1), typeName, comment));
		}

		return list;
	}


    /**
     * 生成一个表的模板
     * @param name 表名
     * @param outPath 输出位置
     * @param packageName 包名
     * @param isDatabaseName 是不是数据库的名字
     * @param cover
     */
	private void createFile(String name, String outPath, String packageName, boolean isDatabaseName, Connection conn, boolean cover) throws Exception {
        if (isDatabaseName){
            createFile(tableNames(name,conn),outPath,packageName,conn,cover);
            return;
        }
		// 创建引擎
		VelocityEngine ve=new VelocityEngine();
		//设置模板加载路径，这里设置的是class下
//		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "D:\\workspace\\fwk\\velocity\\src");
		Properties p = new Properties();
        p.setProperty("input.encoding", "UTF-8");
        p.setProperty("output.encoding", "UTF-8");
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //进行初始化操作
        ve.init(p);
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        VelocityContext context = new VelocityContext();
        String firestUpTableName = commitString(name.substring(0, 1).toUpperCase() + name.substring(1));
        context.put("JdbcList",contextLoads(name,true,conn));
        context.put("MysqlJdbcList",contextLoads(name,false,conn));
        context.put("AllPackgeName",packageName);
        context.put("TableClassName",firestUpTableName);
        context.put("TableDownClassName",commitString(name));
        context.put("TableName",name);
        context.put("IServicePackageName",packageName + ".Service");
        context.put("ServicePackageName",packageName + ".Service.impl");
        context.put("ModelPackageName",packageName + ".model");
        context.put("Date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        context.put("CreateMan","ekkoSu");
//		//设置输出
        System.out.println("开始生成" + name +"的文件");
        int size = 0;
        for (int i = 0; i < vmFiles.length; i++) {
            String fileName = vmFiles[i];
            String vmPackageFile = vmPackageFiles[i];
            String outFile = outFileNameReplace(fileName,firestUpTableName);
            String fileLocal = outPath + packageName.replace(".", "/") + vmPackageFile + outFile;
            File file = createTempFile(fileLocal, true);
            context.put("PackageName",packageName+vmPackageFile.replace("/",".").substring(0,vmPackageFile.lastIndexOf("/")));
            context.put("ClassName",outFile.replace(".xml",""));
            if(file == null){
                System.out.println(fileLocal+"已存在 跳过");
                continue;
            }
            PrintWriter writer = new PrintWriter(file);
            String tempFilePath = vmFilePath + fileName;
            System.out.println(tempFilePath);
            System.out.println("生成 :" + outFile + "\n" + ve.mergeTemplate(tempFilePath,"UTF-8",context,writer) +"\n位置" + fileLocal);
            writer.close();
            size++;
        }
        System.out.println("生成"+size+"个文件");
	}



    /**
     *
     * @param names 表名们
     * @param outpath 输出位置
     * @param packgeName 包名
     * @throws SQLException
     */
    private void createFile(List<String> names,String outpath,String packgeName,Connection conn,boolean cover) throws Exception {
        for (String name : names) {
            createFile(name,outpath,packgeName,false,conn, cover);
        }
    }

    /**
     * 修改数字库名字匹配java命名规则
     * @param s
     * @return
     */
    private String commitString(String s){
        s = s.trim();
        boolean b = false;
        StringBuffer sb = new StringBuffer();
        for (char aChar : s.toCharArray()) {
            if(b){
                b = false;
                aChar -= 32;
            }
            if('_' == aChar){
               b=true;
               continue;
            }

            sb.append(aChar);

        }
        return sb.toString();
    }

	/**
	 * 文件名修改
	 * @param file 文件名
	 * @param className 类名
	 * @return
	 */
	private String outFileNameReplace(String file,String className){
		String outFile = file.substring(0,file.lastIndexOf("."));
		if("xml".equals(outFile)){
			outFile = className + "." + outFile;
		}else if ("IService".equals(outFile)){
			outFile = "I" + className + outFile.substring(1) +".java";
		}else{
			outFile = className + outFile + ".java";
		}
		return outFile;
	}

    /**
     * 根据database 找到所有table
     * @param DataBaseName 迷你工资
     * @return 表名
     * @throws SQLException
     */
    private List<String> tableNames(String DataBaseName,Connection conn) throws SQLException {
        List<String> list = new ArrayList<>();
        DatabaseMetaData data = conn.getMetaData();
        ResultSet tables = data.getTables(null, null, null, new String[]{"TABLE"});
        while (tables.next()){
            list.add(tables.getString(3));
        }
        return list;
    }

    /**
     * 创建文件
     */
    private File createTempFile(String fileLocal, boolean cover) throws IOException {
        int endIndex = fileLocal.lastIndexOf("/");
        if(endIndex<=0) {
            endIndex = fileLocal.lastIndexOf("\\");
        }
        String path = fileLocal.substring(0, endIndex);
        String filename = fileLocal.substring(endIndex+1);

        File dir=new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File file=new File(path+"/"+filename);
        if(!file.exists()) {
            file.createNewFile();
        }else{
            if (cover){
                file.createNewFile();
            }else{
                return null;
            }
        }
        return file;
    }

}
