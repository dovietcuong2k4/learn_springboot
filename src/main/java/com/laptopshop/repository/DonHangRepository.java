package com.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.laptopshop.entities.DonHang;
import com.laptopshop.entities.NguoiDung;

public interface DonHangRepository extends JpaRepository<DonHang, Long>, QuerydslPredicateExecutor<DonHang> {

	public List<DonHang> findByTrangThaiDonHangAndShipper(String trangThai, NguoiDung shipper);

	@Query(value = "select DATE_FORMAT(dh.ngay_nhan_hang, '%m') as month, "
			+ " DATE_FORMAT(dh.ngay_nhan_hang, '%Y') as year, "
			+ " sum(ct.so_luong_nhan_hang * ct.don_gia) as total "
			+ " from don_hang dh join chi_tiet_don_hang ct"
			+ " where dh.id = ct.ma_don_hang and dh.trang_thai_don_hang = 'Hoàn thành' "
			+ " group by DATE_FORMAT(dh.ngay_nhan_hang, '%Y'), DATE_FORMAT(dh.ngay_nhan_hang, '%m') "
			+ " order by year asc",
			nativeQuery = true)
	public List<Object> layDonHangTheoThangVaNam();
	
	public List<DonHang> findByNguoiDat(NguoiDung ng);
	
	public int countByTrangThaiDonHang(String trangThaiDonHang);
	
}
