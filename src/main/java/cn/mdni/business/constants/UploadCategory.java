package cn.mdni.business.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * <dl>
 * <dd>Description: 文件上传的枚举类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/12 下午5:50</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public enum UploadCategory {

	CONTRACT("contract"), UEDITOR("ueditor"), NOTICEBOARD("noticeboard"), EMPLOYEE_PHOTO("employee"), CHAIRMAN_MAIBOX(
			"chairmanMaibox"), APPLY_ATTACHMENT("attachment"), PDF("pdf"), EXCLE("xls");

	// 文件保存的目录
	private String path;

	UploadCategory(String path) {
		this.path = path;
	}

	public static UploadCategory parsePathToCategory(String path) {
		for (UploadCategory category : UploadCategory.values()) {
			if (StringUtils.equalsIgnoreCase(path, category.getPath())) {
				return category;
			}
		}
		return null;
	}

	public String getPath() {
		return path;
	}
}
