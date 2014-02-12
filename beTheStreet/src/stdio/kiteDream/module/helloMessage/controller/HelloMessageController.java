package stdio.kiteDream.module.helloMessage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import stdio.kiteDream.module.helloMessage.bean.HelloMessage;
import stdio.kiteDream.module.helloMessage.service.HelloMessageService;
import stdio.kiteDream.module.userEvent.service.UserEventService;
import stdio.kiteDream.module.vo.JsonVO;
import stdio.kiteDream.util.Constant;

@Controller
@RequestMapping("/api/helloMessage")
public class HelloMessageController {

	@Autowired
	private HelloMessageService messageService;
	@Autowired
	private UserEventService userEventService;

	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public JsonVO get(ModelMap model,@RequestParam(value="userid",required=false) int userid) {
		JsonVO json = new JsonVO();
		try {
			json.setErrorcode(Constant.OK);
			json.setResult(messageService.getMessages());
			json.setUser_events(userEventService.checkEvent(userid));
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<HelloMessage> list(ModelMap model) {
		try {
			return messageService.getMessages();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<HelloMessage>();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/add",  method = { RequestMethod.GET, RequestMethod.POST })
	public boolean add(ModelMap model, @RequestParam("msg") String msg,
			/*@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,*/
			@RequestParam("title") String title) {
		try {
			HelloMessage message = new HelloMessage();
			message.setTitle(title);
			message.setInfo(msg);
			/*message.setStartTime(Constant.DAY.parse(startTime));
			message.setEndTime(Constant.DAY.parse(endTime));*/
			return messageService.saveMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@ResponseBody
	@RequestMapping(value = "/update", method = { RequestMethod.GET, RequestMethod.POST })
	public boolean update(ModelMap model, @RequestParam("msg") String msg,
			@RequestParam("id") String id,
			/*@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,*/
			@RequestParam("title") String title) {
		try {
			HelloMessage message = messageService.getMessage(id);
			message.setTitle(title);
			message.setInfo(msg);
			/*message.setStartTime(Constant.DAY.parse(startTime));
			message.setEndTime(Constant.DAY.parse(endTime));*/
			return messageService.saveMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/del/{msgId}", method = RequestMethod.GET)
	public boolean sendCmd(ModelMap model, @PathVariable("msgId") String msgId) {
		try {
			return messageService.deleteMessage(msgId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
