package com.zhita.model.manage;

public class CommodityFootprintCopy {
    private Integer id;

    private Integer userid;

    private String footprint;

    private String footprinttime;

    private String deleted;

    private String company;
    
    private String onesourcename;

    private String twosourcename;

    public CommodityFootprintCopy(Integer id, Integer userid, String footprint, String footprinttime, String deleted, String company,String onesourcename,String twosourcename) {
        this.id = id;
        this.userid = userid;
        this.footprint = footprint;
        this.footprinttime = footprinttime;
        this.deleted = deleted;
        this.company = company;
        this.onesourcename = onesourcename;
        this.twosourcename = twosourcename;
    }

    public CommodityFootprintCopy() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFootprint() {
        return footprint;
    }

    public void setFootprint(String footprint) {
        this.footprint = footprint == null ? null : footprint.trim();
    }

    public String getFootprinttime() {
        return footprinttime;
    }

    public void setFootprinttime(String footprinttime) {
        this.footprinttime = footprinttime == null ? null : footprinttime.trim();
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }
    
    public String getOnesourcename() {
        return onesourcename;
    }

    public void setOnesourcename(String onesourcename) {
        this.onesourcename = onesourcename == null ? null : onesourcename.trim();
    }

    public String getTwosourcename() {
        return twosourcename;
    }

    public void setTwosourcename(String twosourcename) {
        this.twosourcename = twosourcename == null ? null : twosourcename.trim();
    }
}