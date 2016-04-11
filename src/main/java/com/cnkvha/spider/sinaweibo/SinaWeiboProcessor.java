package com.cnkvha.spider.sinaweibo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class SinaWeiboProcessor implements PageProcessor {

	private final Site site = Site.me().setRetryTimes(2).setSleepTime(1000).setCharset("UTF-8")
			.addCookie("wb_publish_vip_1619819552", "1")
			.addCookie("YF-Page-G0", "aabeaa17d9557111c805fb15a9959531")
			.addCookie("YF-Ugrow-G0", "169004153682ef91866609488943c77f")
			.addCookie("_s_tentry", "-")
			.addCookie("Apache", "7024234114435.954.1460371588350")
			.addCookie("wvr", "6")
			.addCookie("SINAGLOBAL", "2184617727086.9883.1460370434913")
			.addCookie("ULV", "1460371588427:2:2:2:7024234114435.954.1460371588350:1460371573999")
			.addCookie("SUB", "_2AkMgVw-7f8NhqwJRmP0XzGzkbYx3yAjEiebDAHzsJxJjHiEG7DxnqKh30hn6S_MDIuZk9QOweU8ecUcb")
			.addCookie("SUBP", "0033WrSXqPxfM72-Ws9jqgMF55529P9D9WWCkQUpJpCb7J3ScVIYhcfy")
			.addCookie("UOR", ",,www.ltesting.net")
			.addCookie("login_sid_t", "9e9f89993cbc144866d66f93b3b770b7");
	
	public Site getSite() {
		return site;
	}

	private final static String USERNAME_START = "value=user_name\">";
	private final static String USERNAME_END = "</a>";
	
	public void process(Page page) {
		SpiderMain.getInstance().getLogger().info("Processing " + page.getRequest().getUrl());
		
		for(String s : page.getHtml().$("script").all()){
			if(!s.startsWith("<script>FM.view({\"ns\":\"pl.content.homeFeed.index\"")) continue;
			String h = s.substring(s.indexOf("\"html\":\"") + 8, s.length() - 12)
						.replace("\\n", "\n")
						.replace("\\\"", "\"")
						.replace("\\/", "/")
						.replace("\\r", "")
						.replace("\\t", "");

			Page wb = new Page();
			wb.setRequest(page.getRequest());
			wb.setRawText(h);
			for(Selectable feed : wb.getHtml().$("div.WB_feed").nodes()){
				for(Selectable detail : feed.$("div.WB_feed_detail").nodes()){
					System.out.println(detail.get());
					for(Selectable x : detail.nodes()){
						//Username
						String user = x.xpath("//div[@class='WB_info]/a/text()").get();
						
						//Time
						String time = x.xpath("//div[@class='WB_from']/a[@node-type='feed_list_item_date']/@title").get();
						
						//Content
						String content = x.xpath("//div[@class='WB_text']/text()").get();
						
						System.out.println("[" + time + "] " + user +" => " + content);
						
						//for(String lnk : feed.links().all()){
							//System.out.println(lnk);
						//}
					}
				}
			}
			
			//Potional links
			for(String lnk : wb.getHtml().links().all()){
				System.out.println(lnk);
			}
		}
	}

}
