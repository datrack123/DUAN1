package com.hanni.utils;

import com.hanni.entity.TaiKhoan;

public class Auth {

    //Đối tượng này chứa thông tin người sử dụng sau khi đăng nhập
    public static TaiKhoan user = null; 
    //Xóa thông tin của người sử dụng khi có yêu cầu đăng xuất
    public static void clear() {
        Auth.user = null;
    }
    //Kiểm tra xem đăng nhập hay chưa
    public static boolean isLogin() {
        return Auth.user != null;
    }
     //Kiểm tra xem có phải là Quản lý hay không
    public static boolean isManager() {
        return Auth.isLogin() && user.isVaiTro();
    }
    
}