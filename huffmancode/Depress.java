package huffmancode;
//�ļ���ѹ
import java.io.*;
import java.util.*;

public class Depress {
	
	static int endLen;
    public static void main(String[] args) {
    	System.out.println("��ѹ�ļ�!");
        String zipFile = "D:\\BaiduNetdiskDownload\\����������\\huffmancode\\AfterDepressing.code";
        String dstFile = "D:\\BaiduNetdiskDownload\\����������\\huffmancode\\AfterCoding.docx";
        unZipFile(zipFile, dstFile);
        System.out.println("��ѹ�ɹ�!");
    }

    public static void unZipFile(String zipFile, String dstFile) {
        InputStream is = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(zipFile);
            ois = new ObjectInputStream(is);
            //����ķ����л�,���ļ��ж�ȡ����
            byte[] huffmanBytes = (byte[]) ois.readObject();
            Map<Byte, String> huffmanCodes = (Map<Byte, String>) ois.readObject();
            endLen = (int)ois.readObject();
            byte[] bytes = huffmanUnzip(huffmanCodes, huffmanBytes);
            os = new FileOutputStream(dstFile);
            os.write(bytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                os.close();
                ois.close();
                is.close();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
        }
    }

    //��������ѹ
    static byte[] huffmanUnzip(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < huffmanBytes.length; i++) {
            byte b = huffmanBytes[i];
            boolean flag = (i == huffmanBytes.length - 1);//����Ƿ񵽱�������һλ
            stringBuilder.append(byteToBitString(!flag, b));
        }

        //����,��������,����ֵ�Է�����
        HashMap<String, Byte> map = new HashMap<>();
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }

        //���ݱ���ɨ�赽��Ӧ��ASCLL���Ӧ���ַ�
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < stringBuilder.length(); ) {
            int count = 1;
            boolean flag = true;
            Byte b = null;
            while (flag) {
                String key = stringBuilder.substring(i, i + count);
                b = map.get(key);
                if (b == null) {//�����hash�����Ҳ�����Ӧ�ı���,�����Ӵ��ĳ���
                    count++;
                } else {//�ҵ��ˣ��˳�ѭ��
                    flag = false;
                }
            }
            list.add(b);//�������Ӧ���ֽ���list
            i += count;//�������ַ����е�����
        }

        byte b[] = new byte[list.size()];//��list�е��ֽ�ת��Ϊ�ֽ�����
        for (int i = 0; i < b.length; i++) {
            b[i] = list.get(i);
        }
        return b;

    }

  //ת��������
    static String byteToBitString(boolean flag, byte b) {
  	int temp = b;//��bת��Ϊint
  	temp|=256;
  	 String str = Integer.toBinaryString(temp);// ���ص���temp��Ӧ�Ķ����Ʋ���
       if (flag || (flag == false && endLen  == 0)) {
     //�ַ����Ľ�ȡ��ֻ�ú��λ
           return str.substring(str.length() - 8);
       } else {
     //����8bit�ж���λ�ö���λ
           return str.substring(str.length() - endLen);
       }
     
    }
}
