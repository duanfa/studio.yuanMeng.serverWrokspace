package stdio.kiteDream.module.prize.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import stdio.kiteDream.module.comic.bean.BasePathJsonParser;
import stdio.kiteDream.module.prize.bean.Order;
import stdio.kiteDream.module.prize.bean.Order.OrderStatu;
import stdio.kiteDream.module.prize.bean.Prize;
import stdio.kiteDream.module.prize.service.PrizeService;
import stdio.kiteDream.module.userEvent.service.UserEventService;
import stdio.kiteDream.module.vo.JsonVO;
import stdio.kiteDream.module.vo.PageVO;
import stdio.kiteDream.util.Constant;
import stdio.kiteDream.util.ImageUtil;

@Controller
@RequestMapping("/api/prize")
public class PrizeController {
	@Autowired
	PrizeService prizeService;
	@Autowired
	UserEventService userEventService;

	@ResponseBody
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public JsonVO list(HttpServletRequest request,
			@RequestParam(value = "userid", required = true) int userid,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "size", required = false) int size) {
		if (BasePathJsonParser.basePath == null) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			BasePathJsonParser.basePath = basePath;
		}
		JsonVO json = new JsonVO();
		try {
			if (page < 1) {
				json.setResult(prizeService.getPrizes());
			} else {
				json.setResult(prizeService.getPrizes(page, size));
			}
			json.setErrorcode(Constant.OK);
			json.setUser_events(userEventService.checkEvent(userid));
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}
	@ResponseBody
	@RequestMapping(value = "/listPage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public PageVO listPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "size", required = false) int size) {
		if (BasePathJsonParser.basePath == null) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			BasePathJsonParser.basePath = basePath;
		}
		PageVO json = new PageVO();
		try {
			if (page < 1) {
				json.setResult(prizeService.getPrizes());
			} else {
				json.setResult(prizeService.getPrizes(page, size));
			}
			json.setErrorcode(Constant.OK);
			json.setCount(prizeService.getCount());
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/order/list/{userid}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public JsonVO listOrder(HttpServletRequest request,
			@PathVariable(value = "userid") int userid,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "size", required = true) int size) {
		if (BasePathJsonParser.basePath == null) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			BasePathJsonParser.basePath = basePath;
		}
		JsonVO json = new JsonVO();
		try {
			json.setResult(prizeService.getUserOrders(userid, page, size));
			json.setErrorcode(Constant.OK);
			json.setUser_events(userEventService.checkEvent(userid));
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}
	@ResponseBody
	@RequestMapping(value = "/order/listPage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public PageVO listPage(HttpServletRequest request,
			@RequestParam(value = "userid") int userid,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "size", required = true) int size) {
		if (BasePathJsonParser.basePath == null) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			BasePathJsonParser.basePath = basePath;
		}
		PageVO json = new PageVO();
		try {
			json.setResult(prizeService.getUserOrders(userid, page, size));
			json.setErrorcode(Constant.OK);
			json.setCount(prizeService.getOrderCount(userid));
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = { RequestMethod.POST },produces="text/plain;charset=UTF-8")
	public String add(HttpServletRequest request, HttpSession session,
			Prize prize) {
		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			ServletContext context = session.getServletContext();
//			String realContextPath = context.getRealPath("/");
			String realContextPath = Constant.REAL_PATH_PRE;
			String imgPre = "";
			String fileName = "";
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {

					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						fileName = file.getOriginalFilename();
						if (StringUtils.isBlank(fileName)) {
							if (prize.getId()>0) {
								Prize oldPrize = prizeService.getPrize(prize.getId()+"");
								prize.setHeadPhoto(oldPrize.getHeadPhoto());
								prize.setThumbnail_path(oldPrize.getThumbnail_path());
							}
							break;
						}else{
							if (prize.getId()>0) {
								try {
									Prize oldPrize = prizeService.getPrize(prize.getId()+"");
									File oldHeadPhoto = new File(realContextPath+"/"+oldPrize.getHeadPhoto());
									File oldThumbnail_path = new File(realContextPath+"/"+oldPrize.getThumbnail_path());
									if(oldHeadPhoto.isFile()){
										oldHeadPhoto.delete();
									}
									if(oldThumbnail_path.isFile()){
										oldThumbnail_path.delete();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						imgPre = Constant.COMIC_PATH_PRE;
						File localFile = new File(realContextPath + "/"
								+ imgPre + fileName);
						while (localFile.exists()) {
							imgPre = Constant.COMIC_PATH_PRE
									+ new Date().getTime() + "_";
							localFile = new File(realContextPath + "/" + imgPre
									+ fileName);
						}
						file.transferTo(localFile);

						ImageUtil.createThumbnail(localFile, realContextPath
								+ "/" + imgPre + "thumbnail_" + fileName);
						System.out.println(localFile.getAbsolutePath());

						prize.setHeadPhoto(imgPre + fileName);
						prize.setThumbnail_path(imgPre + "thumbnail_"
								+ fileName);

						prize.setHeadPhoto(imgPre + fileName);
						prize.setThumbnail_path(imgPre + "thumbnail_" + fileName);
					}

				}
				prizeService.savePrize(prize);
			}
			return "{\"result\":\"success\",\"info\":\"none\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"fail\",\"info\":\"" + e.getMessage() + "\"}";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete/{prizeid}", method = RequestMethod.GET)
	public boolean del(HttpServletRequest request, HttpSession session,
			@PathVariable("prizeid") String prizeid) {
		try {
			ServletContext context = session.getServletContext();
			String realContextPath = context.getRealPath("/");
			return prizeService.deletePrize(prizeid,realContextPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@ResponseBody
	@RequestMapping(value = "/order/delete/{id}", method = RequestMethod.GET)
	public boolean deleteOrder(@PathVariable("id") int id) {
		try {
			return prizeService.deleteOrder(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/buy/{prizeid}", method = RequestMethod.GET)
	public JsonVO buy(HttpServletRequest request,
			@PathVariable("prizeid") int prizeid,
			@RequestParam("userid") int userid,
			Order order) {
		JsonVO json = new JsonVO();
		try {
			int statu = prizeService.manageBuy(userid, prizeid, order);
			switch (statu) {
			case 1:
				json.setErrorcode(Constant.OK);
				break;
			case 2:
				json.setErrorcode("SALEOUT");
				break;
			case 3:
				json.setErrorcode("SHORT");
				break;
			default:
				json.setErrorcode(Constant.FAIL);
			}
			json.setUser_events(userEventService.checkEvent(userid));
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/cancel/{orderid}", method = RequestMethod.GET)
	public JsonVO cancelPrize(HttpServletRequest request,
			@PathVariable("orderid") int orderid,
			@RequestParam("userid") int userid) {
		JsonVO json = new JsonVO();
		try {
			if(prizeService.manageChangeOrder(orderid, OrderStatu.CANCELLED)){
				json.setErrorcode(Constant.OK);
			}
			json.setUser_events(userEventService.checkEvent(userid));
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/changeOrder/{orderid}", method = {RequestMethod.GET,RequestMethod.POST})
	public JsonVO changeOrder(@PathVariable("orderid") int orderid,
			@RequestParam("statu") OrderStatu statu) {
		JsonVO json = new JsonVO();
		try {
			if(prizeService.manageChangeOrder(orderid,statu)){
				json.setErrorcode(Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}

}