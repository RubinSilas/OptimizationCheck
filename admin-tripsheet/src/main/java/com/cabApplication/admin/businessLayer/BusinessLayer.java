package com.cabApplication.admin.businessLayer;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cabApplication.admin.dataLayer.ComplaintsService;
import com.cabApplication.admin.entity.BookingRequest;
import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.SourceBO;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.entity.UserComplaintsBO;
@Component
public class BusinessLayer {

	@Autowired
	ComplaintsService service;
	public TripCabInfo save(TripCabInfo info) {

		return this.service.save(info);
	}

	public List<UserComplaintsBO>  getComplaintsScreen() {

		return this.service.getComplaintsScreen();
	}

	

	public BookingRequest updateRemark(long bookingId, String remark) {
		// TODO Auto-generated method stub
		return this.service.updateRemarks(bookingId,remark);
	}

	public Long getCount() {
		
		return this.service.getcount();
	}

	public List<SourceBO> getSource() {
		// TODO Auto-generated method stub
		return this.service.getSource();
	}

	public List<Destination> getDestination() {
		// TODO Auto-generated method stub
		return this.service.getDestination();
	}

public List<UserComplaintsBO> getBySearch(String text, long skip, int limit) {
		
		return this.service.getBySearch(text,skip,limit);
	}


}
