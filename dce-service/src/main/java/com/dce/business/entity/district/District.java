package com.dce.business.entity.district;

public class District {
   
	//区域id
    private Integer districtid;

   //区域地址名称
    private String distrctname;

   //区域管理员id
    private Integer userId;

    //是否赋予区域
    private Integer districtstatus;

    
    public Integer getDistrictid() {
        return districtid;
    }

   
    public void setDistrictid(Integer districtid) {
        this.districtid = districtid;
    }

    public String getDistrctname() {
        return distrctname;
    }

    
    public void setDistrctname(String distrctname) {
        this.distrctname = distrctname;
    }

    
    public Integer getUserId() {
        return userId;
    }

  
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    
    public Integer getDistrictstatus() {
        return districtstatus;
    }

    public void setDistrictstatus(Integer districtstatus) {
        this.districtstatus = districtstatus;
    }
}