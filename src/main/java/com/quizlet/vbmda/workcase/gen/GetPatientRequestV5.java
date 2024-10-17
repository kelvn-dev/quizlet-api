package net.vinbrain.vbmda.workcase.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GetPatientRequestV5
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-09T14:52:44.174818+07:00[Asia/Ho_Chi_Minh]")

public class GetPatientRequestV5   {
  @JsonProperty("pid")
  private String pid;

  @JsonProperty("name")
  private String name;

  @JsonProperty("fromDate")
  private Long fromDate;

  @JsonProperty("toDate")
  private Long toDate;

  @JsonProperty("limit")
  private Integer limit;

  @JsonProperty("offset")
  private Integer offset;

  @JsonProperty("tenantCodes")
  @Valid
  private List<String> tenantCodes = null;

  public GetPatientRequestV5 pid(String pid) {
    this.pid = pid;
    return this;
  }

  /**
   * Get pid
   * @return pid
  */
  @ApiModelProperty(value = "")


  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public GetPatientRequestV5 name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GetPatientRequestV5 fromDate(Long fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  /**
   * Get fromDate
   * @return fromDate
  */
  @ApiModelProperty(value = "")


  public Long getFromDate() {
    return fromDate;
  }

  public void setFromDate(Long fromDate) {
    this.fromDate = fromDate;
  }

  public GetPatientRequestV5 toDate(Long toDate) {
    this.toDate = toDate;
    return this;
  }

  /**
   * Get toDate
   * @return toDate
  */
  @ApiModelProperty(value = "")


  public Long getToDate() {
    return toDate;
  }

  public void setToDate(Long toDate) {
    this.toDate = toDate;
  }

  public GetPatientRequestV5 limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Get limit
   * @return limit
  */
  @ApiModelProperty(value = "")


  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public GetPatientRequestV5 offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  /**
   * Get offset
   * @return offset
  */
  @ApiModelProperty(value = "")


  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public GetPatientRequestV5 tenantCodes(List<String> tenantCodes) {
    this.tenantCodes = tenantCodes;
    return this;
  }

  public GetPatientRequestV5 addTenantCodesItem(String tenantCodesItem) {
    if (this.tenantCodes == null) {
      this.tenantCodes = new ArrayList<>();
    }
    this.tenantCodes.add(tenantCodesItem);
    return this;
  }

  /**
   * Get tenantCodes
   * @return tenantCodes
  */
  @ApiModelProperty(value = "")


  public List<String> getTenantCodes() {
    return tenantCodes;
  }

  public void setTenantCodes(List<String> tenantCodes) {
    this.tenantCodes = tenantCodes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPatientRequestV5 getPatientRequestV5 = (GetPatientRequestV5) o;
    return Objects.equals(this.pid, getPatientRequestV5.pid) &&
        Objects.equals(this.name, getPatientRequestV5.name) &&
        Objects.equals(this.fromDate, getPatientRequestV5.fromDate) &&
        Objects.equals(this.toDate, getPatientRequestV5.toDate) &&
        Objects.equals(this.limit, getPatientRequestV5.limit) &&
        Objects.equals(this.offset, getPatientRequestV5.offset) &&
        Objects.equals(this.tenantCodes, getPatientRequestV5.tenantCodes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pid, name, fromDate, toDate, limit, offset, tenantCodes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPatientRequestV5 {\n");
    
    sb.append("    pid: ").append(toIndentedString(pid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    tenantCodes: ").append(toIndentedString(tenantCodes)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

