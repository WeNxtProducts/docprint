package com.wenxt.docprint.security;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lm_menu_users")
public class LM_MENU_USERS {
	
	@Id
	@Column(name="user_id")
	private String	userId;
	

	@Column(name="user_passwd")
	private String	user_passwd;
	
	@Column(name="user_desc")
	private String	user_desc;
	
	@Column(name="user_group_id")
	@Nullable
	private String	user_group_id;
	
	@Column(name="user_bl_desc")
	@Nullable
	private String	user_bl_desc;
	
	@Column(name="user_start_menu_id")
	@Nullable
	private String	user_start_menu_id;
	
	@Column(name="user_ask_passwd_flag")
	@Nullable
	private String	user_ask_passwd_flag;
	
	@Column(name="user_change_passwd_flag")
	@Nullable
	private String	user_change_passwd_flag;
	
	@Column(name="user_disable_flag")
	@Nullable
	private String	user_disable_flag;
	
	@Column(name="user_default_printer")
	@Nullable
	private String	user_default_printer;
	
	@Column(name="user_rep_userid_passwd")
	@Nullable
	private String	user_rep_userid_passwd;
	
	@Column(name="user_tel_no")
	@Nullable
	private String	user_tel_no;
	
	@Column(name="user_tel_extn")
	@Nullable
	private String	user_tel_extn;
	
	@Column(name="user_fax_no")
	@Nullable
	private String	user_fax_no;
	
	@Column(name="user_login_count")
	@Nullable
	private Short user_login_count;
	
	@Column(name="user_ext_yn")
	@Nullable
	private String	user_ext_yn;
	
	@Column(name="user_vps")
	@Nullable
	private String	user_vps;
	
	@Column(name="user_copies")
	@Nullable
	private Short	user_copies;
	
	@Column(name="user_prn_code")
	@Nullable
	private String	user_prn_code;
	
	@Column(name="user_pf_code")
	@Nullable
	private String	user_pf_code;
	
	@Column(name="user_override_yn")
	@Nullable
	private String	user_override_yn;
	
	@Column(name="user_ins_Dt")
	@Nullable
	private LocalDateTime	user_ins_Dt;
	
	@Column(name="user_ins_id")
	@Nullable
	private String	user_ins_id;
	
	@Column(name="user_mod_Dt")
	@Nullable
	private LocalDateTime user_mod_Dt;
	
	@Column(name="user_mod_id")
	@Nullable
	private String	user_mod_id;
	
	@Column(name="user_comp_code")
	@Nullable
	private String	user_comp_code;
	
	@Column(name="user_administrator_yn")
	@Nullable
	private String	user_administrator_yn;
	
	@Column(name="user_email_id")
	private String	userEmailId;
	
	@Column(name="user_mobile_no")
	@Nullable
	private String	user_mobile_no;
	
	@Column(name="user_bl_disp_yn_num")
	@Nullable
	private String	user_bl_disp_yn_num;
	
	@Column(name="user_vps_flag")
	@Nullable
	private String	user_vps_flag;
	
	@Column(name="user_res_tel_no")
	@Nullable
	private String	user_res_tel_no;
	
	@Column(name="user_remarks")
	@Nullable
	private String	user_remarks;
	
	@Column(name="user_expiry_day_count")
	@Nullable
	private Integer	user_expiry_day_count;
	
	@Column(name="user_passwd_expiry_yn")
	@Nullable
	private String	user_passwd_expiry_yn;
	
	@Column(name="user_expiry_dt")
	private LocalDateTime user_expiry_dt;
	
	@Column(name="user_first_login_yn")
	private Boolean userFirstLoginyn;
	
	@Column(name="user_frz_flag")
	@Nullable
	private String	user_frz_flag;
	
	@Column(name="user_initial")
	@Nullable
	private String	user_initial;
	
	@Column(name="user_designation")
	@Nullable
	private String	user_designation;
	
	@Column(name="user_desig_desc")
	@Nullable
	private String	user_desig_desc;
	
	@Column(name="user_logon_option")
	@Nullable
	private String	user_logon_option;
	
	@Column(name="user_status")
	@Nullable
	private String	user_status;
	
	@Column(name="user_eff_to_dt")
	@Nullable
	private String	user_eff_to_dt;
	
	@Column(name="user_ter_reason")
	@Nullable
	private String	user_ter_reason;
	
	@Column(name="user_credit_card_upd_yn")
	@Nullable
	private String	user_credit_card_upd_yn;
	
	@Column(name="user_valid_upto")
	@Nullable
	private String	user_valid_upto;
	
	@Column(name="user_pass_exp_date")
	@Nullable
	private String	user_pass_exp_date;
	
	@Column(name="user_fstlog_flag")
	@Nullable
	private String	user_fstlog_flag;
	
	@Column(name="user_locked_yn")
	@Nullable
	private String	user_locked_yn;
	
	@Column(name="user_no_of_failed_logins")
	@Nullable
	private String	user_no_of_failed_logins;
	
	@Column(name="user_lock_reason")
	@Nullable
	private String	user_lock_reason;
	
	@Column(name="user_email")
	@Nullable
	private String	user_email;
	
	@Column(name="user_port_code")
	@Nullable
	private String	user_port_code;
	
	@Column(name="user_client_id")
	@Nullable
	private String	user_client_id;
	
	@Column(name="user_client_code")
	@Nullable
	private String	user_client_code;
	
	@Column(name="user_dft_company_code")
	@Nullable
	private String	user_dft_company_code;
	
	@Column(name="user_pwd_remind_num")
	@Nullable
	private String	user_pwd_remind_num;
	
	@Column(name="user_pwd_wrong_allow_num")
	@Nullable
	private String	user_pwd_wrong_allow_num;
	
	@Column(name="user_pwd_wrong_limit_yn")
	@Nullable
	private String	user_pwd_wrong_limit_yn;
	
	@Column(name="user_security_code")
	@Nullable
	private String	user_security_code;
	
	@Column(name="user_inst_code")
	@Nullable
	private String	user_inst_code;
	
	@Column(name="user_ds_group_id")
	@Nullable
	private String	user_ds_group_id;
	
	@Column(name="user_pwd_reuse_count")
	@Nullable
	private Double	user_pwd_reuse_count;
	
	@Column(name="user_dflt_lang_code")
	@Nullable
	private String	user_dflt_lang_code;
	
	@Column(name="user_signature")
	@Nullable
	private String	user_signature;
	
	@Column(name="user_reset_token")
	private String userResettoken;
	
	@Column(name = "user_warning_days")
    private Double user_warning_days;
	
	
	public String getUserResettoken() {
		return userResettoken;
	}
	public void setUserResettoken(String userResettoken) {
		this.userResettoken = userResettoken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUser_passwd() {
		return user_passwd;
	}
	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}
	public String getUser_group_id() {
		return user_group_id;
	}
	public void setUser_group_id(String user_group_id) {
		this.user_group_id = user_group_id;
	}
	public String getUser_desc() {
		return user_desc;
	}
	public void setUser_desc(String user_desc) {
		this.user_desc = user_desc;
	}
	public String getUser_bl_desc() {
		return user_bl_desc;
	}
	public void setUser_bl_desc(String user_bl_desc) {
		this.user_bl_desc = user_bl_desc;
	}
	public String getUser_start_menu_id() {
		return user_start_menu_id;
	}
	public void setUser_start_menu_id(String user_start_menu_id) {
		this.user_start_menu_id = user_start_menu_id;
	}
	public String getUser_ask_passwd_flag() {
		return user_ask_passwd_flag;
	}
	public void setUser_ask_passwd_flag(String user_ask_passwd_flag) {
		this.user_ask_passwd_flag = user_ask_passwd_flag;
	}
	public String getUser_change_passwd_flag() {
		return user_change_passwd_flag;
	}
	public void setUser_change_passwd_flag(String user_change_passwd_flag) {
		this.user_change_passwd_flag = user_change_passwd_flag;
	}
	public String getUser_disable_flag() {
		return user_disable_flag;
	}
	public void setUser_disable_flag(String user_disable_flag) {
		this.user_disable_flag = user_disable_flag;
	}
	public String getUser_default_printer() {
		return user_default_printer;
	}
	public void setUser_default_printer(String user_default_printer) {
		this.user_default_printer = user_default_printer;
	}
	public String getUser_rep_userid_passwd() {
		return user_rep_userid_passwd;
	}
	public void setUser_rep_userid_passwd(String user_rep_userid_passwd) {
		this.user_rep_userid_passwd = user_rep_userid_passwd;
	}
	public String getUser_tel_no() {
		return user_tel_no;
	}
	public void setUser_tel_no(String user_tel_no) {
		this.user_tel_no = user_tel_no;
	}
	public String getUser_tel_extn() {
		return user_tel_extn;
	}
	public void setUser_tel_extn(String user_tel_extn) {
		this.user_tel_extn = user_tel_extn;
	}
	public String getUser_fax_no() {
		return user_fax_no;
	}
	public void setUser_fax_no(String user_fax_no) {
		this.user_fax_no = user_fax_no;
	}
	public String getUser_ext_yn() {
		return user_ext_yn;
	}
	public void setUser_ext_yn(String user_ext_yn) {
		this.user_ext_yn = user_ext_yn;
	}
	public String getUser_vps() {
		return user_vps;
	}
	public void setUser_vps(String user_vps) {
		this.user_vps = user_vps;
	}
	public Short getUser_copies() {
		return user_copies;
	}
	public String getUser_prn_code() {
		return user_prn_code;
	}
	public void setUser_prn_code(String user_prn_code) {
		this.user_prn_code = user_prn_code;
	}
	public String getUser_pf_code() {
		return user_pf_code;
	}
	public void setUser_pf_code(String user_pf_code) {
		this.user_pf_code = user_pf_code;
	}
	public String getUser_override_yn() {
		return user_override_yn;
	}
	public void setUser_override_yn(String user_override_yn) {
		this.user_override_yn = user_override_yn;
	}
	public LocalDateTime getUser_ins_Dt() {
		return user_ins_Dt;
	}
	public void setUser_ins_Dt(LocalDateTime user_ins_Dt) {
		this.user_ins_Dt = user_ins_Dt;
	}
	public String getUser_ins_id() {
		return user_ins_id;
	}
	public void setUser_ins_id(String user_ins_id) {
		this.user_ins_id = user_ins_id;
	}
	public LocalDateTime getUser_mod_Dt() {
		return user_mod_Dt;
	}
	public void setUser_mod_Dt(LocalDateTime user_mod_Dt) {
		this.user_mod_Dt = user_mod_Dt;
	}
	public String getUser_mod_id() {
		return user_mod_id;
	}
	public void setUser_mod_id(String user_mod_id) {
		this.user_mod_id = user_mod_id;
	}
	public String getUser_comp_code() {
		return user_comp_code;
	}
	public void setUser_comp_code(String user_comp_code) {
		this.user_comp_code = user_comp_code;
	}
	public String getUser_administrator_yn() {
		return user_administrator_yn;
	}
	public void setUser_administrator_yn(String user_administrator_yn) {
		this.user_administrator_yn = user_administrator_yn;
	}
	
	public String getUser_mobile_no() {
		return user_mobile_no;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public void setUser_mobile_no(String user_mobile_no) {
		this.user_mobile_no = user_mobile_no;
	}
	public String getUser_bl_disp_yn_num() {
		return user_bl_disp_yn_num;
	}
	public void setUser_bl_disp_yn_num(String user_bl_disp_yn_num) {
		this.user_bl_disp_yn_num = user_bl_disp_yn_num;
	}
	public String getUser_vps_flag() {
		return user_vps_flag;
	}
	public void setUser_vps_flag(String user_vps_flag) {
		this.user_vps_flag = user_vps_flag;
	}
	public String getUser_res_tel_no() {
		return user_res_tel_no;
	}
	public void setUser_res_tel_no(String user_res_tel_no) {
		this.user_res_tel_no = user_res_tel_no;
	}
	public String getUser_remarks() {
		return user_remarks;
	}
	public void setUser_remarks(String user_remarks) {
		this.user_remarks = user_remarks;
	}
	public Integer getUser_expiry_day_count() {
		return user_expiry_day_count;
	}
	public void setUser_expiry_day_count(Integer user_expiry_day_count) {
		this.user_expiry_day_count = user_expiry_day_count;
	}
	public String getUser_passwd_expiry_yn() {
		return user_passwd_expiry_yn;
	}
	public void setUser_passwd_expiry_yn(String user_passwd_expiry_yn) {
		this.user_passwd_expiry_yn = user_passwd_expiry_yn;
	}
	

	
	
	
	public LocalDateTime getUser_expiry_dt() {
		return user_expiry_dt;
	}
	public void setUser_expiry_dt(LocalDateTime user_expiry_dt) {
		this.user_expiry_dt = user_expiry_dt;
	}
	public void setUser_login_count(Short user_login_count) {
		this.user_login_count = user_login_count;
	}
	public void setUser_copies(Short user_copies) {
		this.user_copies = user_copies;
	}
	public Boolean getUserFirstLoginyn() {
		return userFirstLoginyn;
	}
	public void setUserFirstLoginyn(Boolean userFirstLoginyn) {
		this.userFirstLoginyn = userFirstLoginyn;
	}
	public String getUser_frz_flag() {
		return user_frz_flag;
	}
	public void setUser_frz_flag(String user_frz_flag) {
		this.user_frz_flag = user_frz_flag;
	}
	public String getUser_initial() {
		return user_initial;
	}
	public void setUser_initial(String user_initial) {
		this.user_initial = user_initial;
	}
	public String getUser_designation() {
		return user_designation;
	}
	public void setUser_designation(String user_designation) {
		this.user_designation = user_designation;
	}
	public String getUser_desig_desc() {
		return user_desig_desc;
	}
	public void setUser_desig_desc(String user_desig_desc) {
		this.user_desig_desc = user_desig_desc;
	}
	public String getUser_logon_option() {
		return user_logon_option;
	}
	public void setUser_logon_option(String user_logon_option) {
		this.user_logon_option = user_logon_option;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getUser_eff_to_dt() {
		return user_eff_to_dt;
	}
	public void setUser_eff_to_dt(String user_eff_to_dt) {
		this.user_eff_to_dt = user_eff_to_dt;
	}
	public String getUser_ter_reason() {
		return user_ter_reason;
	}
	public void setUser_ter_reason(String user_ter_reason) {
		this.user_ter_reason = user_ter_reason;
	}
	public String getUser_credit_card_upd_yn() {
		return user_credit_card_upd_yn;
	}
	public void setUser_credit_card_upd_yn(String user_credit_card_upd_yn) {
		this.user_credit_card_upd_yn = user_credit_card_upd_yn;
	}
	public String getUser_valid_upto() {
		return user_valid_upto;
	}
	public void setUser_valid_upto(String user_valid_upto) {
		this.user_valid_upto = user_valid_upto;
	}
	public String getUser_pass_exp_date() {
		return user_pass_exp_date;
	}
	public void setUser_pass_exp_date(String user_pass_exp_date) {
		this.user_pass_exp_date = user_pass_exp_date;
	}
	public String getUser_fstlog_flag() {
		return user_fstlog_flag;
	}
	public void setUser_fstlog_flag(String user_fstlog_flag) {
		this.user_fstlog_flag = user_fstlog_flag;
	}
	public String getUser_locked_yn() {
		return user_locked_yn;
	}
	public void setUser_locked_yn(String user_locked_yn) {
		this.user_locked_yn = user_locked_yn;
	}
	public String getUser_no_of_failed_logins() {
		return user_no_of_failed_logins;
	}
	public void setUser_no_of_failed_logins(String user_no_of_failed_logins) {
		this.user_no_of_failed_logins = user_no_of_failed_logins;
	}
	public String getUser_lock_reason() {
		return user_lock_reason;
	}
	public void setUser_lock_reason(String user_lock_reason) {
		this.user_lock_reason = user_lock_reason;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_port_code() {
		return user_port_code;
	}
	public void setUser_port_code(String user_port_code) {
		this.user_port_code = user_port_code;
	}
	public String getUser_client_id() {
		return user_client_id;
	}
	public void setUser_client_id(String user_client_id) {
		this.user_client_id = user_client_id;
	}
	public String getUser_client_code() {
		return user_client_code;
	}
	public void setUser_client_code(String user_client_code) {
		this.user_client_code = user_client_code;
	}
	public String getUser_dft_company_code() {
		return user_dft_company_code;
	}
	public void setUser_dft_company_code(String user_dft_company_code) {
		this.user_dft_company_code = user_dft_company_code;
	}
	public String getUser_pwd_remind_num() {
		return user_pwd_remind_num;
	}
	public void setUser_pwd_remind_num(String user_pwd_remind_num) {
		this.user_pwd_remind_num = user_pwd_remind_num;
	}
	public String getUser_pwd_wrong_allow_num() {
		return user_pwd_wrong_allow_num;
	}
	public void setUser_pwd_wrong_allow_num(String user_pwd_wrong_allow_num) {
		this.user_pwd_wrong_allow_num = user_pwd_wrong_allow_num;
	}
	public String getUser_pwd_wrong_limit_yn() {
		return user_pwd_wrong_limit_yn;
	}
	public void setUser_pwd_wrong_limit_yn(String user_pwd_wrong_limit_yn) {
		this.user_pwd_wrong_limit_yn = user_pwd_wrong_limit_yn;
	}
	public String getUser_security_code() {
		return user_security_code;
	}
	public void setUser_security_code(String user_security_code) {
		this.user_security_code = user_security_code;
	}
	public String getUser_inst_code() {
		return user_inst_code;
	}
	public void setUser_inst_code(String user_inst_code) {
		this.user_inst_code = user_inst_code;
	}
	public String getUser_ds_group_id() {
		return user_ds_group_id;
	}
	public void setUser_ds_group_id(String user_ds_group_id) {
		this.user_ds_group_id = user_ds_group_id;
	}
	public Double getUser_pwd_reuse_count() {
		return user_pwd_reuse_count;
	}
	public void setUser_pwd_reuse_count(Double user_pwd_reuse_count) {
		this.user_pwd_reuse_count = user_pwd_reuse_count;
	}
	public String getUser_dflt_lang_code() {
		return user_dflt_lang_code;
	}
	public void setUser_dflt_lang_code(String user_dflt_lang_code) {
		this.user_dflt_lang_code = user_dflt_lang_code;
	}
	public String getUser_signature() {
		return user_signature;
	}
	public void setUser_signature(String user_signature) {
		this.user_signature = user_signature;
	}
	public Double getUser_warning_days() {
		return user_warning_days;
	}
	public void setUser_warning_days(Double user_warning_days) {
		this.user_warning_days = user_warning_days;
	}
	public Short getUser_login_count() {
		return user_login_count;
	}

	
	

}

