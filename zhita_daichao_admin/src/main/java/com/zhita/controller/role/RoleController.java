package com.zhita.controller.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhita.model.manage.Functions;
import com.zhita.model.manage.Role;
import com.zhita.model.manage.SecondFunction;
import com.zhita.service.role.IntRoleService;
import com.zhita.util.PageUtil;

@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	IntRoleService intRoleService;
	
    //后台管理---查询出所有角色信息   含分页
	@ResponseBody
	@RequestMapping("/queryAllRolePage")
    public Map<String, Object> queryAllRolePage(Integer page){
		int totalCount=intRoleService.pageCount();//查询出角色表一共有多少数据
    	PageUtil pageUtil=new PageUtil(page,1,totalCount);
    	if(page<1) {
    		page=1;
    	}
    	else if(page>pageUtil.getTotalPageCount()) {
    		if(totalCount==0) {
    			page=pageUtil.getTotalPageCount()+1;
    		}else {
    			page=pageUtil.getTotalPageCount();
    		}
    	}
    	int pages=(page-1)*pageUtil.getPageSize();
    	pageUtil.setPage(pages);
    	List<Role> list=intRoleService.queryAllRolePage(pageUtil.getPage(), pageUtil.getPageSize());
    	
    	HashMap<String,Object> map=new HashMap<>();
    	map.put("listRole",list);
    	map.put("pageutil", pageUtil);
    	return map;
    }
	
    //后台管理---根据角色状态  模糊查询出所有角色信息   含分页
	@ResponseBody
	@RequestMapping("/queryAllRolePageLike")
    public Map<String, Object> queryAllRolePageLike(String deleted,Integer page){
		List<Role> list=null;
		PageUtil pageUtil=null;
		if(deleted==null||"".equals(deleted)) {
			int totalCount=intRoleService.pageCount();//查询出角色表一共有多少数据
	    	pageUtil=new PageUtil(page,1,totalCount);
	    	if(page<1) {
	    		page=1;
	    	}
	    	else if(page>pageUtil.getTotalPageCount()) {
	    		if(totalCount==0) {
	    			page=pageUtil.getTotalPageCount()+1;
	    		}else {
	    			page=pageUtil.getTotalPageCount();
	    		}
	    	}
	    	int pages=(page-1)*pageUtil.getPageSize();
	    	pageUtil.setPage(pages);
	    	list=intRoleService.queryAllRolePage(pageUtil.getPage(), pageUtil.getPageSize());
		}else {
			int totalCount=intRoleService.pageCountLike(deleted);//根据角色状态  模糊查询出角色表一共有多少数据
			pageUtil=new PageUtil(page,1,totalCount);
			if(page<1) {
				page=1;
			}
			else if(page>pageUtil.getTotalPageCount()) {
				if(totalCount==0) {
					page=pageUtil.getTotalPageCount()+1;
				}else {
					page=pageUtil.getTotalPageCount();
				}
			}
			int pages=(page-1)*pageUtil.getPageSize();
			pageUtil.setPage(pages);
			list=intRoleService.queryAllRolePageLike(deleted, pageUtil.getPage(), pageUtil.getPageSize());
		}
    	
    	HashMap<String,Object> map=new HashMap<>();
    	map.put("listRolelike",list);
    	map.put("pageutil", pageUtil);
    	return map;
    }
	//后台管理---点击添加按钮的时候  先把权限表的所有数据返回给前端
	@ResponseBody
	@RequestMapping("/queryFunctions")
	public List<Functions> queryFunctions(){
		List<Functions> list=intRoleService.queryFunctionAll();//查询出权限表所有的数据
		
		List<Functions> list1=new ArrayList<>();
		
    	for (int i = 0; i < list.size(); i++) {
    		Functions functions=new Functions();
    		functions.setFunctionFirst(list.get(i).getFunctionFirst());
    		
    		String[] idarray=list.get(i).getId().split(",");//将查出来的String类型的一串id  用逗号进行分隔，变成一个数组
    		List<String> idlist=idlist=Arrays.asList(idarray);//将数组变成集合   进行循环
    		List<SecondFunction> listsf=new ArrayList<>();
    		for (int j = 0; j < idlist.size(); j++) {
				SecondFunction sf=new SecondFunction();
				sf.setId(Integer.parseInt(idlist.get(j)));
				String funsecond=intRoleService.queryFunctionsByFunctionId(Integer.parseInt(idlist.get(j)));
				sf.setSecondFunction(funsecond);
				listsf.add(sf);
			}
    		functions.setSecondlist(listsf);
    		list1.add(functions);
		}
    	return list1;
	}
		
	//后台管理--添加角色
	@Transactional
	@ResponseBody
	@RequestMapping("/addRole")
    public void addRole(Role role){
    	int num=intRoleService.addRole(role);
    	List<Functions> listfunction=role.getListfunction();
    	for (int i = 0; i < listfunction.size(); i++) {
    		for (int j = 0; j < listfunction.get(i).getSecondlist().size(); j++) {
    			intRoleService.add(role.getId(),listfunction.get(i).getSecondlist().get(j).getId());
			}
		}
    }
	//后台管理----查看权限功能--通过角色id查询出  当前角色的所有权限
	@ResponseBody
	@RequestMapping("/queryFunctionByRoleid")
    public List<Functions> queryFunctionByRoleid(Integer roleid){
		List<Functions> list=intRoleService.queryFunctionByRoleid(roleid);
		List<Functions> list1=new ArrayList<>();
		
    	for (int i = 0; i < list.size(); i++) {
    		Functions functions=new Functions();
    		functions.setFunctionFirst(list.get(i).getFunctionFirst());
    		
    		String[] idarray=list.get(i).getId().split(",");//将查出来的String类型的一串id  用逗号进行分隔，变成一个数组
    		List<String> idlist=idlist=Arrays.asList(idarray);//将数组变成集合   进行循环
    		List<SecondFunction> listsf=new ArrayList<>();
    		for (int j = 0; j < idlist.size(); j++) {
				SecondFunction sf=new SecondFunction();
				sf.setId(Integer.parseInt(idlist.get(j)));
				String funsecond=intRoleService.queryFunctionsByFunctionId(Integer.parseInt(idlist.get(j)));
				sf.setSecondFunction(funsecond);
				listsf.add(sf);
			}
    		functions.setSecondlist(listsf);
    		list1.add(functions);
		}
    	return list1;
    }
	//后台管理----编辑功能——通过角色id  查询出角色信息
	@ResponseBody
	@RequestMapping("/selectByPrimaryKey")
    public Map<String,Object> selectByPrimaryKey(Integer id) {
    	Role role=intRoleService.selectByPrimaryKey(id);//通过角色id 查询角色信息
		List<Functions> list=intRoleService.queryFunctionAll();//查询出权限表所有的数据
		
		List<Functions> list1=new ArrayList<>();
		
    	for (int i = 0; i < list.size(); i++) {
    		Functions functions=new Functions();
    		functions.setFunctionFirst(list.get(i).getFunctionFirst());
    		
    		String[] idarray=list.get(i).getId().split(",");//将查出来的String类型的一串id  用逗号进行分隔，变成一个数组
    		List<String> idlist=idlist=Arrays.asList(idarray);//将数组变成集合   进行循环
    		List<SecondFunction> listsf=new ArrayList<>();
    		for (int j = 0; j < idlist.size(); j++) {
				SecondFunction sf=new SecondFunction();
				sf.setId(Integer.parseInt(idlist.get(j)));
				String funsecond=intRoleService.queryFunctionsByFunctionId(Integer.parseInt(idlist.get(j)));
				sf.setSecondFunction(funsecond);
				listsf.add(sf);
			}
    		functions.setSecondlist(listsf);
    		list1.add(functions);
		}
    	
		List<Functions> lists=intRoleService.queryFunctionByRoleid(id);
		List<Functions> lists1=new ArrayList<>();
		
    	for (int i = 0; i < lists.size(); i++) {
    		Functions functions=new Functions();
    		functions.setFunctionFirst(lists.get(i).getFunctionFirst());
    		
    		String[] idarray=lists.get(i).getId().split(",");//将查出来的String类型的一串id  用逗号进行分隔，变成一个数组
    		List<String> idlist=idlist=Arrays.asList(idarray);//将数组变成集合   进行循环
    		List<SecondFunction> listsf=new ArrayList<>();
    		for (int j = 0; j < idlist.size(); j++) {
				SecondFunction sf=new SecondFunction();
				sf.setId(Integer.parseInt(idlist.get(j)));
				String funsecond=intRoleService.queryFunctionsByFunctionId(Integer.parseInt(idlist.get(j)));
				sf.setSecondFunction(funsecond);
				listsf.add(sf);
			}
    		functions.setSecondlist(listsf);
    		lists1.add(functions);
		}
    	
    	
    	
    	HashMap<String, Object> map=new HashMap<>();
    	map.put("role",role);
    	map.put("listall",list1);
    	map.put("listByRoleid",lists1);
    	return map;
    }
	//后台管理---保存编辑后的信息
	@Transactional
	@ResponseBody
	@RequestMapping("/upaBaocun")
	public int upaBaocun(Role role){
		List<Integer> list=intRoleService.queryAllIdByRoleid(role.getId());
		if(list.size()!=0) {
			for (int i = 0; i < list.size(); i++) {
				intRoleService.delFunctionByid(list.get(i));
			}
		}
		
	  	List<Functions> listfunction=role.getListfunction();
    	for (int i = 0; i < listfunction.size(); i++) {
    		for (int j = 0; j < listfunction.get(i).getSecondlist().size(); j++) {
    			intRoleService.add(role.getId(),listfunction.get(i).getSecondlist().get(j).getId());
			}
		}
		int num=intRoleService.upaRole(role);
		return num;
	}
}
