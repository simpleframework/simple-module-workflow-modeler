package net.simpleframework.workflow.modeler;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.HttpClient;
import net.simpleframework.workflow.remote.AbstractWorkflowRemoteHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultModelerRemoteHandler extends AbstractWorkflowRemoteHandler {

	private final String remote_page = StringUtils.text(
			ModelerSettings.get().getProperty("remote_page"),
			"/sf/workflow-web-remote-ModelerRemotePage");

	private String jsessionid;

	@Override
	public Map<String, Object> call(final String url, final String method,
			final Map<String, Object> data) throws IOException {
		final HttpClient httpClient = new HttpClient(url).setJsessionid(jsessionid);
		final Map<String, Object> json = httpClient.post(remote_page + "?method=" + method, data);
		final String _jsessionid = (String) json.get("jsessionid");
		if (StringUtils.hasText(_jsessionid)) {
			jsessionid = _jsessionid;
		}
		return json;
	}
}
