package com.sinotrans.samsung.activity;

public class GenJson {	
	
		private String fileName;// 文件名
		private String fileContent;// 文件的内容
		private String fileType;
		private Long orderId;//订单id
		private String consignorCode;//客户编码
	    private String orderCode;//订单编码
		public String getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
		}

		public Long getOrderId() {
			return orderId;
		}

		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}

		public String getConsignorCode() {
			return consignorCode;
		}

		public void setConsignorCode(String consignorCode) {
			this.consignorCode = consignorCode;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileContent() {
			return fileContent;
		}

		public void setFileContent(String fileContent) {
			this.fileContent = fileContent;
		}
}
