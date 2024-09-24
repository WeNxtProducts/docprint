package com.wenxt.docprint.service;

import com.wenxt.docprint.model.ReportBuilderRequest;

public interface LjmRepwhereService {

	String LjmRepwhereCreates(ReportBuilderRequest reportBuilderRequest);

	String updatelLjmRepwhere(ReportBuilderRequest reportBuilderRequest, Long LJM_REP_SYS_ID);

	String deleteLjmRepwhere(Long LJM_REP_SYS_ID);

	String getLjmRepwhere(Long LJM_REP_SYS_ID) throws Exception;

}
