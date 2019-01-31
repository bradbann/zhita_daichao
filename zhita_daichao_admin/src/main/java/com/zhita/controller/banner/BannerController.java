package com.zhita.controller.banner;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zhita.model.manage.Advertising;
import com.zhita.model.manage.LoanClassification;
import com.zhita.model.manage.ShufflingFigure;
import com.zhita.service.banner.IntBannerService;
import com.zhita.util.OssUtil;
import com.zhita.util.PageUtil;

@Controller
@RequestMapping("/banner")
public class BannerController {
	@Resource(name="bannerServiceImp")
	 private IntBannerService intBannerService;

	public IntBannerService getIntBannerService() {
		return intBannerService;
	}

	public void setIntBannerService(IntBannerService intBannerService) {
		this.intBannerService = intBannerService;
	}
    //后台管理---查询轮播图全部信息,含分页
	@ResponseBody
	@RequestMapping("/queryAll")
    public Map<String,Object> queryAll(Integer page,String[] company){
		PageUtil pageUtil=null;
		List<ShufflingFigure> list=new ArrayList<>();
		if(company.length==1) {
			
			System.out.println("company.length==1");
			
	    	int totalCount=intBannerService.pageCount(company[0]);//该方法是查询轮播图总数量
	    	pageUtil=new PageUtil(page,2,totalCount);
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
	    	list=intBannerService.queryAll(company[0],pageUtil.getPage(),pageUtil.getPageSize());
		}
		else if(company.length>1) {
			
			System.out.println("company.length>1");
			
    		int totalCountfor=0;
    		List<ShufflingFigure> listfor=null;
			for (int i = 0; i < company.length; i++) {
		    	int totalCountfor1=intBannerService.pageCount(company[i]);//该方法是查询轮播图总数量
            	totalCountfor=totalCountfor+totalCountfor1;
            	
            	System.out.println("totalCountfor："+totalCountfor);
		    	pageUtil=new PageUtil(page,2,totalCountfor);
		    	if(page<1) {
		    		page=1;
		    	}
		    	else if(page>pageUtil.getTotalPageCount()) {
		    		if(totalCountfor==0) {
		    			page=pageUtil.getTotalPageCount()+1;
		    		}else {
		    			page=pageUtil.getTotalPageCount();
		    		}
		    	}
		    	int pages=(page-1)*pageUtil.getPageSize();
		    	pageUtil.setPage(pages);
		    	listfor=intBannerService.queryAll(company[i],pageUtil.getPage(),pageUtil.getPageSize());
            	list.addAll(listfor);
			}
		}
    	HashMap<String,Object> map=new HashMap<>();
    	map.put("listshuff",list);
    	map.put("pageutil", pageUtil);
    	return map;
    }
    //后台管理---根据标题字段模糊查询轮播图信息，含分页
	@ResponseBody
	@RequestMapping("/queryAllByLike")
    public Map<String,Object> queryAllByLike(String title,Integer page,String company){
		List<ShufflingFigure> list=null;
		PageUtil pageUtil=null;
		if(title==null||"".equals(title)) {
	    	int totalCount=intBannerService.pageCount(company);//该方法是查询轮播图总数量
	    	pageUtil=new PageUtil(page,2,totalCount);
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
	    	list=intBannerService.queryAll(company,pageUtil.getPage(),pageUtil.getPageSize());
		}else {
	    	int totalCount=intBannerService.pageCountByLike(title,company);//该方法是根据标题模糊查询轮播图总数量
	    	pageUtil=new PageUtil(page,2,totalCount);
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
	    	list=intBannerService.queryAllByLike(title,company,pageUtil.getPage(),pageUtil.getPageSize());
		}
    	HashMap<String,Object> map=new HashMap<>();
    	map.put("listshuffByLike",list);
    	map.put("pageutil", pageUtil);
    	return map;
    }
    //后台管理---添加轮播图信息
	@Transactional
	@ResponseBody
	@RequestMapping("/AddAll")
    public Map<String, Object> AddAll(ShufflingFigure shufflingFigure,MultipartFile file) throws Exception{
		Map<String, Object> map = new HashMap<>();
		int count=intBannerService.ifBusinessNameIfExist(shufflingFigure.getBusinessname());
		if(count==0) {
			map.put("noexist", 0);//说明文本框的商家名称是不正确的
		}else {
			map.put("exist", 1);//说明文本框的商家名称是正确的
		}
		if (file != null) {// 判断上传的文件是否为空
			String path = null;// 文件路径
			String type = null;// 文件类型
			InputStream iStream = file.getInputStream();
			String fileName = file.getOriginalFilename();// 文件原名称
			// 判断文件类型
			type = fileName.indexOf(".") != -1? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()): null;
			if (type != null) {// 判断文件类型是否为空
				if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
					// 自定义的文件名称
					String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
					// 设置存放图片文件的路径
					path = "shuffling_figure/" + /* System.getProperty("file.separator")+ */trueFileName;
					OssUtil ossUtil = new OssUtil();
					String ossPath = ossUtil.uploadFile(iStream, path);
					if(ossPath.substring(0, 5).equals("https")) {
						System.out.println("路径为："+ossPath);
						shufflingFigure.setCover(ossPath);
						map.put("msg", "图片上传成功");
					}
					
					System.out.println("存放图片文件的路径:" + ossPath);
				} else {
					map.put("msg", "不是我们想要的文件类型,请按要求重新上传");
					return map;
				}
			} else {
				map.put("msg", "文件类型为空");
				return map;
			}
		}else {
			map.put("msg", "请上传图片");
			return map;
		} 
    	intBannerService.AddAll(shufflingFigure);
    	return map;
    }
    //后台管理 ---根据主键id查询出轮播图信息
	@ResponseBody
	@RequestMapping("/selectByPrimaryKey")
    public ShufflingFigure selectByPrimaryKey(Integer id) {
    	ShufflingFigure shufflingFigure=intBannerService.selectByPrimaryKey(id);
    	return shufflingFigure;
    }
    //后台管理---通过传过来的轮播图对象，对当前对象进行修改保存
	@Transactional
	@ResponseBody
	@RequestMapping("/updateShufflingFigure")
    public Map<String, Object> updateShufflingFigure(ShufflingFigure shufflingFigure,MultipartFile file) throws Exception{
		Map<String, Object> map = new HashMap<>();
		if (file != null) {// 判断上传的文件是否为空
			String path = null;// 文件路径
			String type = null;// 文件类型
			InputStream iStream = file.getInputStream();
			String fileName = file.getOriginalFilename();// 文件原名称
			// 判断文件类型
			type = fileName.indexOf(".") != -1? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()): null;
			if (type != null) {// 判断文件类型是否为空
				if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
					// 自定义的文件名称
					String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
					// 设置存放图片文件的路径
					path = "shuffling_figure/" + /* System.getProperty("file.separator")+ */trueFileName;
					OssUtil ossUtil = new OssUtil();
					String ossPath = ossUtil.uploadFile(iStream, path);
					if(ossPath.substring(0, 5).equals("https")) {
						System.out.println("路径为："+ossPath);
						shufflingFigure.setCover(ossPath);
						map.put("msg", "图片上传成功");
					}
					
					System.out.println("存放图片文件的路径:" + ossPath);
				} else {
					map.put("msg", "不是我们想要的文件类型,请按要求重新上传");
					return map;
				}
			} else {
				map.put("msg", "文件类型为空");
				return map;
			}
		}else {
			int id = shufflingFigure.getId();
			String cover = intBannerService.getCover(id); //通过传过来的轮播图id，查询图片的URL
			shufflingFigure.setCover(cover);
		} 
    	intBannerService.updateShufflingFigure(shufflingFigure);
    	return map;
    }
    //后台管理---根据删除按钮，修改轮播图假删除状态
	@Transactional
	@ResponseBody
	@RequestMapping("/upaFalseDel")
    public int upaFalseDel(Integer id) {
    	int num=intBannerService.upaFalseDel(id);
    	return num;
    }
	//后台管理---根据前端传过来的状态值，对当前对象的状态值进行修改
	@Transactional
	@ResponseBody
	@RequestMapping("/upaState")
	public int upaState(String state,Integer id) {
		int num=0;
		if(state.equals("1")) {
			num=intBannerService.upaStateOpen(id);
		}else {
			num=intBannerService.upaStateClose(id);
		}
		return num;
	}
	
}
