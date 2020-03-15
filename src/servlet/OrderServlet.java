package servlet;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.Session;

import dao.OrderDao;
import dao.OrderDaoImp;
import dao.YesDao;
import dao.YesDaoImp;
import domain.Order;
import domain.Yes;
import util.*;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String name =request.getParameter("name");
		String ID =request.getParameter("ID");
		String phone = request.getParameter("phone");
		String number = request.getParameter("number");
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		OrderDao orderDao = new OrderDaoImp();
		YesDao yesDao = new YesDaoImp();
		List<Yes> lists= new ArrayList<Yes>();
		lists.add(yesDao.get("yes1", ID));
		lists.add(yesDao.get("yes1", ID));
		lists.add(yesDao.get("yes3", ID));
		try {
			if(IDCardValidate(ID))
			{
				if(phoneValidate(phone))
				{
					if(isWin(lists))
					{

						Order order = new Order();
						order.setName(name);
						order.setId(ID);
						order.setPhone(phone);
						order.setNum(Integer.parseInt(number));
						order.setOrderid(uuid);
						if(orderDao.add(order))
						{
							//Ԥ���ɹ�
						}
						else {
							//Ԥ��ʧ�ܣ����������֤������绰���룬����ʧ����֤��ԤԼ����
						}
					}
					else {
						//ǰ����ԤԼ��ȡ�ɹ�
					}
				}
				else {
					//�绰�������
				}
			}
			else {
				//���֤����
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public boolean phoneValidate(String phone) {
		if(phone.length()!=11)
		{
			return false;
		}
		return true;
		
	}
	public boolean isWin(List<Yes> lists)
	{
		for(Yes yes:lists)
		{
			if(yes.getComfired()==1)
			{
				return false;
			}
		}
		return true;
	}
	public static boolean IDCardValidate(String IDStr) throws ParseException {          
        String Ai = "";  
        // �жϺ���ĳ��� 15λ��18λ  
        if (IDStr.length() != 15 && IDStr.length() != 18) {  
            return false;  
        }  
          
  
        // 18λ���֤ǰ17λλ���֣������15λ�����֤�����к��붼Ϊ����  
        if (IDStr.length() == 18) {  
            Ai = IDStr.substring(0, 17);  
        } else if (IDStr.length() == 15) {  
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);  
        }  
        if (isNumeric(Ai) == false) {  
            return false;  
        }  
          
  
        // �жϳ��������Ƿ���Ч   
        String strYear = Ai.substring(6, 10);// ���  
        String strMonth = Ai.substring(10, 12);// �·�  
        String strDay = Ai.substring(12, 14);// ����  
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {  
            return false;  
        }  
        GregorianCalendar gc = new GregorianCalendar();  
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");  
        try {  
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150  
                    || (gc.getTime().getTime() - s.parse(  
                            strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {  
                return false;  
            }  
        } catch (NumberFormatException e) {  
            e.printStackTrace();  
        } catch (java.text.ParseException e) {  
            e.printStackTrace();  
        }  
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {  
            return false;  
        }  
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {  
            return false;  
        }  
          
  
        // �жϵ������Ƿ���Ч   
        Hashtable areacode = GetAreaCode();  
        //������֤ǰ��λ�ĵ����벻��Hashtable�������������  
        if (areacode.get(Ai.substring(0, 2)) == null) {  
            return false;  
        }  
          
        if(isVarifyCode(Ai,IDStr)==false){  
            return false;  
        }  
         
          
        return true;  
    }  
      
      
     /* 
      * �жϵ�18λУ�����Ƿ���ȷ 
     * ��18λУ����ļ��㷽ʽ��  
        ����1. ��ǰ17λ���ֱ������Ȩ���  
        ������ʽΪ��S = Sum(Ai * Wi), i = 0, ... , 16  
        ��������Ai��ʾ��i��λ���ϵ����֤��������ֵ��Wi��ʾ��iλ���ϵļ�Ȩ���ӣ����λ��Ӧ��ֵ����Ϊ�� 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2  
        ����2. ��11�Լ�����ȡģ  
        ����Y = mod(S, 11)  
        ����3. ����ģ��ֵ�õ���Ӧ��У����  
        ������Ӧ��ϵΪ��  
        ���� Yֵ��     0  1  2  3  4  5  6  7  8  9  10  
        ����У���룺 1  0  X  9  8  7  6  5  4  3   2 
     */  
    private static boolean isVarifyCode(String Ai,String IDStr) {  
         String[] VarifyCode = { "1", "0", "X", "9", "8", "7", "6", "5", "4","3", "2" };  
         String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7","9", "10", "5", "8", "4", "2" };  
         int sum = 0;  
         for (int i = 0; i < 17; i++) {  
            sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);  
         }  
         int modValue = sum % 11;  
         String strVerifyCode = VarifyCode[modValue];  
         Ai = Ai + strVerifyCode;  
         if (IDStr.length() == 18) {  
             if (Ai.equals(IDStr) == false) {  
                 return false;  
                  
             }  
         }   
         return true;  
    }  
      
  
    /** 
     * �����е�ַ���뱣����һ��Hashtable��     
     * @return Hashtable ���� 
     */  
     
    private static Hashtable<String, String> GetAreaCode() {  
        Hashtable<String, String> hashtable = new Hashtable<String, String>();  
        hashtable.put("11", "����");  
        hashtable.put("12", "���");  
        hashtable.put("13", "�ӱ�");  
        hashtable.put("14", "ɽ��");  
        hashtable.put("15", "���ɹ�");  
        hashtable.put("21", "����");  
        hashtable.put("22", "����");  
        hashtable.put("23", "������");  
        hashtable.put("31", "�Ϻ�");  
        hashtable.put("32", "����");  
        hashtable.put("33", "�㽭");  
        hashtable.put("34", "����");  
        hashtable.put("35", "����");  
        hashtable.put("36", "����");  
        hashtable.put("37", "ɽ��");  
        hashtable.put("41", "����");  
        hashtable.put("42", "����");  
        hashtable.put("43", "����");  
        hashtable.put("44", "�㶫");  
        hashtable.put("45", "����");  
        hashtable.put("46", "����");  
        hashtable.put("50", "����");  
        hashtable.put("51", "�Ĵ�");  
        hashtable.put("52", "����");  
        hashtable.put("53", "����");  
        hashtable.put("54", "����");  
        hashtable.put("61", "����");  
        hashtable.put("62", "����");  
        hashtable.put("63", "�ຣ");  
        hashtable.put("64", "����");  
        hashtable.put("65", "�½�");  
        hashtable.put("71", "̨��");  
        hashtable.put("81", "���");  
        hashtable.put("82", "����");  
        hashtable.put("91", "����");  
        return hashtable;  
    }  
  
    /** 
     * �ж��ַ����Ƿ�Ϊ����,0-9�ظ�0�λ��߶��    
     * @param strnum 
     * @return 
     */  
    private static boolean isNumeric(String strnum) {  
        Pattern pattern = Pattern.compile("[0-9]*");  
        Matcher isNum = pattern.matcher(strnum);  
        if (isNum.matches()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
  
    /** 
     * ���ܣ��ж��ַ������������Ƿ����������ʽ�����������գ����ꡢƽ���ÿ��31�졢30������µ�28�����29�� 
     *  
     * @param string 
     * @return 
     */  
    public static boolean isDate(String strDate) {  
      
        Pattern pattern = Pattern  
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");  
        Matcher m = pattern.matcher(strDate);  
        if (m.matches()) {  
            return true;  
        } else {  
            return false;  
        }  
    }

}
