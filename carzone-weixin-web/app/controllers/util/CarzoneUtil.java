package controllers.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CarzoneUtil {

	public static void main(String[] args) {

	}

	/**
	 * ����ǩ���õ���
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String[] getNowDateTimeByFormatForGetUpSign() {
		String returnStr[] = getNowDateTimeByFormat("yyyy-MM-dd HH:mm:ss")
				.split(" ");
		return returnStr;
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return
	 */
	// yyyy��m��d�� HH��m��
	// yyyy��M��d��
	public static String getNowDateTimeByFormat(String dataFormat) {
		DateFormat df = new SimpleDateFormat(dataFormat);
		return df.format(new Date());
	}

	public static String getFormatTime(Date date, String formatStr) {
		DateFormat df = new SimpleDateFormat(formatStr);
		return df.format(date);
	}

	/**
	 * ��ָ�����ڸ�ʽ��
	 * 
	 * @param date
	 *            ָ������
	 * @return
	 */
	public static String formatDate(String date) {
		String result = "";
		try {
			DateFormat df1 = new SimpleDateFormat("yyyy��M��d��");
			Calendar c = Calendar.getInstance();
			c.setTime(df1.parse(date));

			DateFormat df2 = new SimpleDateFormat("yyyy��");
			result = df2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ֽ�����ת��Ϊʮ������ַ�
	 * 
	 * @param bytearray
	 * @return
	 */
	public static String byteToStr(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexStr(bytearray[i]);
		}
		return strDigest;
	}

	/**
	 * ���ֽ�ת��Ϊʮ������ַ�
	 * 
	 * @param ib
	 * @return
	 */
	public static String byteToHexStr(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];

		String s = new String(ob);
		return s;
	}

	/**
	 * XMLת����map
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		// ������
		InputStream inputStream = request.getInputStream();
		// ͨ��SAX����������
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// �õ�xml��Ԫ��
		Element root = document.getRootElement();
		// �õ���Ԫ�ص������ӽڵ�
		List<Element> elementList = root.elements();

		// ���������ӽڵ�
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		// �ͷ���Դ
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * �ж��ַ��Ƿ�Ϊ����
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * ����ֽڽ�ȡ�ַ�
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String byteSubstring(String s, int length) {
		String returnStr = null;
		byte[] bytes = null;
		int i = 2; // Ҫ��ȡ���ֽ���ӵ�3���ֽڿ�ʼ
		try {
			bytes = s.getBytes("Unicode");
			int n = 0; // ��ʾ��ǰ���ֽ���
			for (; i < bytes.length && n < length; i++) {
				// ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�
				if (i % 2 == 1) {
					n++; // ��UCS2�ڶ����ֽ�ʱn��1
				} else {
					// ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�
					if (bytes[i] != 0) {
						n++;
					}
				}
			}
			// ���iΪ����ʱ�������ż��
			if (i % 2 == 1) {
				// ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ���
				if (bytes[i - 1] != 0)
					i = i - 1;
				// ��UCS2�ַ�����ĸ�����֣��������ַ�
				else
					i = i + 1;
			}
			returnStr = new String(bytes, 0, i, "Unicode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	public static String byteSubstring(String str) {
		try {
			if (str.getBytes("utf-8").length > 2037) {
				str = CarzoneUtil.byteSubstring(str, 1358) + "\n......";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * ͨ��HTTP��ȡJSON�ַ�
	 * 
	 * @param requestUrl
	 * @param charFormat
	 * @return
	 */
	public static String getJsonByHttp(String requestUrl, String charFormat) {
		StringBuffer returnStrBuffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// �����ص�������ת�����ַ�
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, charFormat);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				returnStrBuffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStrBuffer.toString();
	}

}
