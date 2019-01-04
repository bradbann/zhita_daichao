package com.zhita.service.merchant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhita.dao.manage.SourceMapper;
import com.zhita.model.manage.Source;
import com.zhita.model.manage.User;
import com.zhita.util.PageUtil;

@Service
public class MerchantServiceImp implements IntMerchantService{
	@Autowired
	private SourceMapper sourceMapper;
	
    //后台管理---查询出所有渠道表信息，不含分页
    public List<Source> queryAll(){
    	List<Source> list=sourceMapper.queryAll();
    	return list;
    }
    //后台管理---通过渠道id，查询渠道信息
    public Source selectByPrimaryKey(Integer id) {
    	Source source=sourceMapper.selectByPrimaryKey(id);
    	return source;
    }
    
    //后台管理---通过传过来的渠道对象，对当前对象进行修改保存
    public int updateSource(Source source) {
    	int num=sourceMapper.updateSource(source);
    	return num;
    }
    
    //后台管理---查询出所有渠道表信息，含分页
    public List<Source> queryAllSource(Integer page,Integer pagesize){
    	List<Source> list=sourceMapper.queryAllSource(page,pagesize);
    	return list;
    }
    
    //后台管理---用于获取总页数
    public int pageCount() {
    	int count=sourceMapper.pageCount();
    	return count;
    }
    
    //后台管理---用于获取模糊查询总页数
    public int pageCountByLike(String sourceName) {
    	int count=sourceMapper.pageCountByLike(sourceName);
    	return count;
    }
    
    //后台管理---模糊查询渠道信息,并且有分页功能
    public List<Source> queryByLike(String sourceName,Integer page,Integer pagesize){
    	List<Source> list=sourceMapper.queryByLike(sourceName, page,pagesize);
    	return list;
    }
    
    //后台管理---添加渠道信息
    public int addAll(Source source) {
    	int num=sourceMapper.addAll(source);
    	return num;
    }
    
    //后台管理---通过删除按钮，改变当前渠道的假删除状态，将状态改为删除
    public  int upaFalseDel(Integer id) {
    	int num=sourceMapper.upaFalseDel(id);
    	return num;
    }
    
    //后台管理---修改渠道状态为开启
    public int upaStateOpen(Integer id) {
    	int num=sourceMapper.upaStateOpen(id);
    	return num;
    }
    
    //后台管理---修改渠道状态为关闭
    public int upaStateClose(Integer id) {
    	int num=sourceMapper.upaStateClose(id);
    	return num;
    }
    
    //后台管理 ---查询出当前渠道id在用户表的姓名，年龄，身份证号，手机号，注册时间 的总数量
    public int pageCountBySourceId(Integer sourceId) {
    	int count=sourceMapper.pageCountBySourceId(sourceId);
    	return count;
    }
    
    
    //后台 管理---查询出当前渠道id在用户表的姓名，年龄，身份证号，手机号，注册时间   含分页
    public List<User> queryAllUserBySourceId(Integer SourceId,Integer page,Integer pagesize){
    	List<User> list=sourceMapper.queryAllUserBySourceId(SourceId, page, pagesize);
    	return list;
    }
    
    
    //后台 管理---通过注册时间 。。手机号    查询出当前渠道id在用户表的姓名，年龄，身份证号，手机号，注册时间   含分页
    public Map<String,Object> queryAllUserByLikeAll(Integer SourceId,String registrationTimeStart,String registrationTimeEnd,String phone,Integer page){
    	List<User> list=null;
    	PageUtil pageUtil=null;
    	//不带条件，查询所有
    	if((SourceId!=null||!"".equals(SourceId))&&(registrationTimeStart==null||"".equals(registrationTimeStart))&&(registrationTimeEnd==null||"".equals(registrationTimeEnd))&&(phone==null||"".equals(phone))) {
    		int totalCount=sourceMapper.pageCountBySourceId(SourceId);
        	pageUtil=new PageUtil(page,2,totalCount);
        	if(page<1) {
        		page=1;
        	}
        	else if(page>pageUtil.getTotalPageCount()) {
        		page=pageUtil.getTotalPageCount();
        	}
        	int pages=(page-1)*pageUtil.getPageSize();
        	pageUtil.setPage(pages);
        	list=sourceMapper.queryAllUserBySourceId(SourceId, pageUtil.getPage(), pageUtil.getPageSize());
    	}
    	//通过注册时间模糊查询
    	else if((SourceId!=null||!"".equals(SourceId))&&(registrationTimeStart!=null||!"".equals(registrationTimeStart))&&(registrationTimeEnd!=null||!"".equals(registrationTimeEnd))&&(phone==null||"".equals(phone))) {
    		int totalCount=sourceMapper.pageCountByRegistrationTime(SourceId, registrationTimeStart, registrationTimeEnd);
        	pageUtil=new PageUtil(page,2,totalCount);
        	if(page<1) {
        		page=1;
        	}
        	else if(page>pageUtil.getTotalPageCount()) {
        		page=pageUtil.getTotalPageCount();
        	}
        	int pages=(page-1)*pageUtil.getPageSize();
        	pageUtil.setPage(pages);
        	list=sourceMapper.queryAllUserByRegistrationTime(SourceId, registrationTimeStart, registrationTimeEnd, pageUtil.getPage(),pageUtil.getPageSize());
    	}
    	//通过电话模糊查询
    	else if((SourceId!=null||!"".equals(SourceId))&&(registrationTimeStart==null||"".equals(registrationTimeStart))&&(registrationTimeEnd==null||"".equals(registrationTimeEnd))&&(phone!=null||!"".equals(phone))) {
    		int totalCount=sourceMapper.pageCountByPhone(SourceId, phone);
    		pageUtil=new PageUtil(page,2,totalCount);
        	if(page<1) {
        		page=1;
        	}
        	else if(page>pageUtil.getTotalPageCount()) {
        		page=pageUtil.getTotalPageCount();
        	}
        	int pages=(page-1)*pageUtil.getPageSize();
        	pageUtil.setPage(pages);
        	list=sourceMapper.queryAllUserByPhone(SourceId, phone,pageUtil.getPage(),pageUtil.getPageSize());
    	}
    	//通过注册时间和电话模糊查询
    	else if((SourceId!=null||!"".equals(SourceId))&&(registrationTimeStart!=null||!"".equals(registrationTimeStart))&&(registrationTimeEnd!=null||!"".equals(registrationTimeEnd))&&(phone!=null||!"".equals(phone))) {
    		int totalCount=sourceMapper.pageCountByRegistrationTimePhone(SourceId, registrationTimeStart, registrationTimeEnd, phone);
    		pageUtil=new PageUtil(page,2,totalCount);
        	if(page<1) {
        		page=1;
        	}
        	else if(page>pageUtil.getTotalPageCount()) {
        		page=pageUtil.getTotalPageCount();
        	}
        	int pages=(page-1)*pageUtil.getPageSize();
        	pageUtil.setPage(pages);
        	list=sourceMapper.queryAllUserByRegistrationTimePhone(SourceId, registrationTimeStart, registrationTimeEnd, phone, pageUtil.getPage(),pageUtil.getPageSize());
    	}
    	HashMap<String, Object> map=new HashMap();
    	map.put("listUserBySourceId",list);
    	map.put("pageutil",pageUtil);
    	return map;
    }
}
