package member.board.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import member.board.dto.MemDto;
import member.board.service.MemService;

@Controller
@SessionAttributes("user")
public class MemController {
	@Autowired
	MemService service;
	
	@ModelAttribute("user")
	public MemDto getDto() {
		return new MemDto();
	}
	
	@GetMapping("/insert")
	public String joinform() {
		return "mem/joinform";
	}
	@GetMapping("/idCheck")
	@ResponseBody
	public String idCheck(String id) {
		String checkid = service.idCheck(id);
		return checkid;
	}
	
	@PostMapping("/insert")
	public String insert(MemDto dto) {
		service.insertMem(dto);
		return "redirect:loginform";
	}
	@GetMapping("/loginform")
	public String loginform() {
		return "mem/loginform";
	}
	@PostMapping("/login")
	public String login(@ModelAttribute("command") @Valid MemDto dto, BindingResult error, Model m) {
		
		if(error.hasErrors()) {
			return "mem/loginform";
		}
		
		MemDto resultDto = service.login(dto);
		if(resultDto == null) {
			error.reject("nocode", "로그인 실패: 아이디나 비밀번호가 틀림");	
			return "mem/loginform";
		}else {//로그인 성공
			m.addAttribute("user", resultDto);
		}
		return "redirect:/main";
	}
	@GetMapping("/logout")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/main";
	}
	
	@GetMapping("/update")
	public String updateform() {
		return "mem/updateform";
	}
	@PutMapping("/update")
	public String update(@ModelAttribute("user") MemDto dto) {
		service.updateMem(dto);
		return "redirect:/main";
	}
	@GetMapping("/delete")
	public String deleteform() {
		return "mem/deleteform";
	}
	
	@GetMapping("/delete/wrongpw")
	public String deleteformError(Model m) {
		m.addAttribute("error", "비밀번호 틀림");
		return "mem/deleteform";
	}
	
	@DeleteMapping("/delete")
	public String delete(String formpw, @ModelAttribute("user") MemDto dto, SessionStatus status) {
	
		int i = service.deleteMem(formpw, dto);
		if(i == 0) {
			return "redirect:/delete/wrongpw";
		}else {
			status.setComplete();
			return "redirect:/main";
		}
	}
	@RequestMapping("/main")
	public String main(@ModelAttribute("user") MemDto dto) {
		if(dto.getId() != null) {
			return "mem/main";
		}else {
			return "mem/loginform";
		}
	}
}





