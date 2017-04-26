##mybatis

### ibatis
1. 目前项目中用的是ibatis
2. isNotNull 和 isNotEmpty的区别：  
	在iBATIS中isNull用于判断参数是否为Null，isNotNull相反。
isEmpty判断参数是否为Null或者空，满足其中一个条件则其true
isNotEmpty相反，当参数既不为Null也不为空是其为true
 判断之后做相对应的表达式操作

如下代码：

	<select id="getCustomerRegNum" resultClass="int" parameterClass="QueryCustomerCondition">  
			          select count(cus_id) from cus_customer_tbl   
			          <dynamic prepend="WHERE">  
			            <isNotEmpty prepend="AND" property="cusWebFrom">  
			                ( CUS_CUSTOMER_TBL.CUS_WEB_FROM LIKE '%$cusWebFrom$%')  
			            </isNotEmpty>  
			            <isNotEmpty prepend="AND" property="cusWebAgent">  
			                ( CUS_CUSTOMER_TBL.CUS_WEB_AGENT LIKE '%$cusWebAgent$%')  
			            </isNotEmpty>  
			          </dynamic>  
			   </select>  

当之传入参数 cusWebForm 而不传入 cusWebAgent 时，产生的SQL语句为：

	select count(cus_id) from cus_customer_tbl    WHERE      ( CUS_CUSTOMER_TBL.CUS_WEB_FROM LIKE '%baidu%')

而当XML代码用<isNotNull > 配置时（注意他们的区别），

	<select id="getCustomerRegNum" resultClass="int" parameterClass="QueryCustomerCondition">  
          select count(cus_id) from cus_customer_tbl   
          <dynamic prepend="WHERE">  
            <isNotNull prepend="AND" property="cusWebFrom">  
                ( CUS_CUSTOMER_TBL.CUS_WEB_FROM LIKE '%$cusWebFrom$%')  
            </isNotNull>  
            <isNotNull prepend="AND" property="cusWebAgent">  
                ( CUS_CUSTOMER_TBL.CUS_WEB_AGENT LIKE '%$cusWebAgent$%')  
            </isNotNull>  
          </dynamic>  
	</select>  

同样 ，当之传入参数 cusWebForm 而不传入 cusWebAgent 时，产生的SQL语句为：

	select count(cus_id) from cus_customer_tbl     WHERE     ( CUS_CUSTOMER_TBL.CUS_WEB_FROM LIKE '%baidu%')      AND        (CUS_CUSTOMER_TBL.CUS_WEB_AGENT LIKE '%%')
	
对于string的参数，用isnotempty  
3. mybatis 和 ibatis的区别  
 
- 以前用的parameterClass在mybatis中已经永不了了，mybatis里应该使用parameterType。另外resultMap里面也不能继续使用了改成了type  
- dynamic标签不能使用了  
- 数据类型的声明和ibatis有了很大的差别，ibatis可以像下面这样写  

```
	insert into M_HEALTHSPECIALYTYPE(FCODE,FCHARGE,FTYPECONTENT,FID,FMARK)   
	values (#FCODE:VARCHAR2#,#FCHARGE:VARCHAR2#,#FTYPECONTENT:VARCHAR2#,#FID#,#FMARK:VARCHAR2#)  
```

在mybatis的话一般是这样弄的

```
	insert into M_HEALTHSPECIALYTYPE(FCODE,FCHARGE,FTYPECONTENT,FID,FMARK)   
     values (#{FCODE,jdbcType=VARCHAR},#{FCHARGE,jdbcType=VARCHAR},#{FTYPECONTENT,jdbcType=VARCHAR},#{FID},#{FMARK,jdbcType=VARCHAR})   
```

- 加了一个叫映射器的新东西，只需要写出接口而不需要实现类就能够操作数据如  

```
	 import java.util.List;
	 import org.lxh.vo.HealthspecialytypeInfo;    
	 public interface HealthMapper {  
    public List<HealthspecialytypeInfo> getRecordByList(HealthspecialytypeInfo info);  
}  
```

在映射文件里要记得namespace写成接口的类全名，sql语句的id写成接口里的方法名就可以了

	<select id="getRecordByList" parameterType="org.lxh.vo.HealthspecialytypeInfo" resultMap="resultMap">   
        select * from M_HEALTHSPECIALYTYPE where FCODE in  
        <foreach item="item" index="index" collection="list"  
          open="(" separator="," close=")">  
          #{item}  
        </foreach>  
    </select>  

调用的时候就像下面

	HealthMapper m=ses.getMapper(HealthMapper.class);  
	List<HealthspecialytypeInfo> record=m.getRecordByList(h);  

就这样就可以直接进行数据的操作了