package com.laptopshop.controller;

import com.laptopshop.dto.ListCongViecDTO;
import com.laptopshop.entities.NguoiDung;
import com.laptopshop.entities.VaiTro;
import com.laptopshop.service.DonHangService;
import com.laptopshop.service.LienHeService;
import com.laptopshop.service.NguoiDungService;
import com.laptopshop.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/staff")
@SessionAttributes("loggedInUser")
public class StaffController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private LienHeService lienHeService;

    @Autowired
    private VaiTroService vaiTroService;

    @ModelAttribute("loggedInUser")
    public NguoiDung loggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return nguoiDungService.findByEmail(auth.getName());
    }

    @GetMapping
    public String staffPage(Model model) {
        ListCongViecDTO listCongViec = new ListCongViecDTO();
        listCongViec.setSoDonHangMoi(donHangService.countByTrangThaiDonHang("Đang chờ giao"));
        listCongViec.setSoDonhangChoDuyet(donHangService.countByTrangThaiDonHang("Chờ duyệt"));
        listCongViec.setSoLienHeMoi(lienHeService.countByTrangThai("Đang chờ trả lời"));

        model.addAttribute("listCongViec", listCongViec);
        return "staff/staffPage";
    }

    @GetMapping("/don-hang")
    public String quanLyDonHangPage(Model model) {
        Set<VaiTro> vaiTro = new HashSet<>();
        // lấy danh sách shipper
        vaiTro.add(vaiTroService.findByTenVaiTro("ROLE_SHIPPER"));
        List<NguoiDung> shippers = nguoiDungService.getNguoiDungByVaiTro(vaiTro);
        for (NguoiDung shipper : shippers) {
            shipper.setListDonHang(donHangService.findByTrangThaiDonHangAndShipper("Đang giao", shipper));
        }
        model.addAttribute("allShipper", shippers);
        return "staff/quanLyDonHang"; // thay cái này
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpServletRequest request) {
        model.addAttribute("user", getSessionUser(request));
        System.out.println(getSessionUser(request).toString());
        return "staff/profile";
    }

    @PostMapping("/profile/update")
    public String updateNguoiDung(@ModelAttribute NguoiDung nd, HttpServletRequest request) {
        NguoiDung currentUser = getSessionUser(request);
        currentUser.setDiaChi(nd.getDiaChi());
        currentUser.setHoTen(nd.getHoTen());
        currentUser.setSoDienThoai(nd.getSoDienThoai());
        nguoiDungService.updateUser(currentUser);
        return "redirect:/staff/profile";
    }

    public NguoiDung getSessionUser(HttpServletRequest request) {
        return (NguoiDung) request.getSession().getAttribute("loggedInUser");
    }
}
