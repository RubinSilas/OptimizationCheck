package com.cabApplication.admin.businessLayer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cabApplication.admin.dataLayer.AssignedCabDL;
import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.entity.SourceBO;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.entity.TripDetails;

@Component
public class AssignedCabBL {
	@Autowired
	AssignedCabDL service;

	public TripCabInfo save(TripCabInfo info) {

		return this.service.save(info);
	}

	public List<TripCabInfo> findAssignedCab() {

		return this.service.findAssignedCab();
	}

	public List<SourceBO> findSource() {

		return this.service.findSource();
	}

	public List<Destination> findDestination() {

		return this.service.findByDestination();
	}

	public Long getCount() {

		return this.service.getCount();
	}
	public List<TripDetails> getAssignedCabByScroll(long skip, int limit) {
		
		return this.service.getAssignedCabByScroll(skip,limit);
	}

	public List<TripDetails> getByFilter(String source, String destination, String timeSlot, long skip, int limit) {
		
		return this.service.getByFilter(source,destination,timeSlot,skip,limit);
	}

	public List<TripDetails> getByTextSearch(String text, long skip, int limit) {
		
		return this.service.getBySearch(text,skip,limit);
	}
//-----------------------------------------Testing Purpose-----------------------------------------------------------
	public SourceBO save(SourceBO source) {

		return this.service.save(source);
	}

	public Destination save(Destination des) {

		return this.service.save(des);
	}

	public List<TripCabInfo> findByCabNumber(String cabNumber) {

		return this.service.findByCabNumber(cabNumber);
	}

	public List<TripCabInfo> findByDestination(String destination) {

		return this.service.findByDestination(destination);
	}

//	public List<TripCabInfo> findByDriverName(String driverName) {
//
//		return this.service.findByDriverName(driverName);
//	}

	public DriverInfo save(DriverInfo driver) {
		
		return this.service.save(driver);
	}

//	public List<TripCabInfo> getByFilter(String source, String destination, String timeSlot, long skip, int limit) {
//		
//		return this.service.getByFilter(source, destination, timeSlot, skip, limit);
//	}



}
