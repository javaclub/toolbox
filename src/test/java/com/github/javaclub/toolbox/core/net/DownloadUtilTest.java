package com.github.javaclub.toolbox.core.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.javaclub.toolbox.core.Strings;
import com.github.javaclub.toolbox.util.FileUtil;

import org.junit.Test;

/**
 * Run this test with a valid Internet connection. NOTE: this .java file is UTF-8 encoded, please open with correct encoding in IDE,
 * otherwise, test will fail for Chinese comparison.
 * 
 * @author Xuefeng
 */
public class DownloadUtilTest {

	@Test
	public void testDownloadSina() throws IOException {
//		HttpResponse response = DownloadUtil.download("http://www.sina.com.cn/");
//		assertTrue(response.isOk());
//		assertTrue(response.isText());
//		assertEquals("gb2312", response.getContentEncoding().toLowerCase());
//		String text = response.getText();
//		assertTrue(text.indexOf("新浪首页") > 0);
		
		HttpResponse response = DownloadUtil.download("http://kde.cnki.net/KDEService/Search/Brief/CJFD/?TI=计算机&AU=王", "gb2312");
		assertTrue(response.isOk());
		assertTrue(response.isText());
		assertEquals("gb2312", response.getContentEncoding().toLowerCase());
		String text = response.getText();
		System.out.println(text);
	}

	@Test
	public void testDownloadDouban() throws IOException {
		HttpResponse response = DownloadUtil.download("http://www.douban.com/");
		assertTrue(response.isOk());
		assertTrue(response.isText());
		assertEquals("utf-8", response.getContentEncoding().toLowerCase());
		String text = response.getText();
		assertTrue(text.indexOf("欢迎来到豆瓣") > 0);
	}

	@Test
	public void testRedirect() throws IOException {
		HttpResponse response = DownloadUtil.download("http://bbs.csdn.net/");
		assertTrue(response.isRedirect());
		assertEquals("http://community.csdn.net", response.getRedirectUrl());
	}
	
	@Test
	public void downImage() throws IOException {
		HttpResponse response = DownloadUtil.download("http://www.db4o.com/downloads/db4o-7.12-java.zip");
		if(response.isBinary()) {
			FileUtil.copy(response.getContentData(), new File("C:/db4o-7.12-java.zip"));
		}
	}
	
	@Test
	public void downURL() throws Exception {
		final AtomicInteger counter = new AtomicInteger();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Fresher(counter)).start();
		}
		
		System.out.println("Go on ...");
		
	}
	
	@Test
	public void fixPicUrls() throws Exception {
		String ids = "14339005399,16373968587,14780541787,15589579719,18221184521,19282088536,15601699360,14150199064,14734727393,12772845668,16931112312,16304960648,16490535553,15431754254,16742983695,15583620920,3266606279,14964664488,17783924771,4049617957,19319792735,10515899381,15719414872,16691043276,19281400578,13627737383,3056664501,18742100682,16821703125,16642911803,16728331621,18650304859,16458335276,14137663660,15587938604,13311471879,10006370745,19327000046,19068184756,4231815427,19192960138,15330330842,8370701794,2204195638,3014834862,16803975522,15644274113,3772358273,4588210762,16217412090,10859848524,5714605940,15578192119,16367923420,18920580127,16205096503,209705374,8502060497,9479236419,12847627405,15906412907,8669304335,16700503449,19204712105,8207722354,17021508499,19278660616,2934539615,16249427419,9766394709,13300338924,9945267237,15586708083,17862676722,16542283867,15914940006,14127754729,8876777744,19046936146,15329547518,13035262752,15410593413,13036293334,16609859982,19210620021,15691138582,15042178932,14749525646,14096897775,12774799925,16480931194,18243836194,17755984823,15226562802,13145539733,9101332715,15589787123,14664694714,14167971827,13095879085,13092719949,12871465449,13598656101,13092531460,16263347795,19209840315";
		String[] idArray = ids.split(",");
		int couner = 0; String tmp = "<img id=\"J_ImgBooth\" src=\"";
		for (int i = 0; i < idArray.length; i++) {
			HttpResponse response = DownloadUtil.download("http://detail.tmall.com/item.htm?id=" + idArray[i]);
			if(response.isRedirect()) {
				response = DownloadUtil.download(response.getRedirectUrl());
				if(response.isText()) {
					String s = response.getText(); 
					if( s.indexOf(tmp) > 0 ) {
						String[] kk = Strings.substringsBetween(s, tmp, "\"");
						if(null != kk && kk.length > 0 && Strings.isNotEmpty(kk[0])) {
							System.out.println(kk[0]);
							// String[] ss = StringUtils.substringsBetween(kk[0], "bao/uploaded/", ".jpg\" ");
							/*if(null != ss && ss.length > 0 && StringUtils.isNotEmpty(ss[0])) {
								System.out.println(idArray[i] + " => " + ss[0] + "\n");
								try {
									// DownloadUtil.download("http://www.mgow.net/plus.php?type=fixpic&iid=" + idArray[i] + "&pic_url=" + ss[0]);
									Thread.sleep(1000L);
								} catch (Exception e) {
									Thread.sleep(1000L);
								}
							}*/
						}
						Thread.sleep(1000L);
						couner++;
					}
				}
			}
		}
		System.out.println(couner);
	}

}

class Fresher implements Runnable {
	
	private AtomicInteger counter;

	public Fresher(AtomicInteger counter) {
		super();
		this.counter = counter;
	}

	public void run() {
		// String url = "http://www.mgow.net/plus.php?type=fixurl&idx=";// 秒购网
		String url = "http://www.0460.com/view/134907.html"; // 王牌嗨购
		HttpResponse response = null;
		for (int i = 1000; i < 10000000; i++) {
			response = DownloadUtil.download(url + String.valueOf(i));
			if(response.isText()) {
				System.out.println(response.getText());
				System.out.println(counter.incrementAndGet());
			}
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				// ignore
			}
		}
		
	}
	
}
