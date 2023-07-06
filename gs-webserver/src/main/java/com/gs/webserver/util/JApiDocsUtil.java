package com.gs.webserver.util;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;

/**
 * @author zhaojz
 * @version 创建时间：2021年1月14日 下午3:31:48 类说明
 */
public class JApiDocsUtil {

	public static void main(String[] args) {
		DocsConfig config = new DocsConfig();
		config.setProjectPath("E:\\Idea\\GS\\gs-cloud\\gs-webserver"); // 项目根目录
		config.setProjectName("简氏小工具系统"); // 项目名称
		config.setApiVersion("V1.0.1.0"); // 声明该API的版本
		config.setDocsPath("f:/temp/project/"); // 生成API 文档所在目录
		config.setAutoGenerate(Boolean.TRUE); // 配置自动生成
		config.addPlugin(new MarkdownDocPlugin()); // 生成markdown格式接口文档
		Docs.buildHtmlDocs(config); // 执行生成文档
	}
}
