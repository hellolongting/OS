package equipment.driver;

import equipment.domain.EquipmentEnum;
import equipment.EquipmentController;
import equipment.domain.Equipment;

/* 
 * 设备管理的方法
 */
public class EquipmentDriver {
	public static Equipment[] equipment = new Equipment[3];
	final static int A_NUM = 2;
	final static int B_NUM = 3;
	final static int C_NUM = 3;
    public static EquipmentController equipmentController;

	// 初始化设备
	static {
		equipment[0] = new Equipment(EquipmentEnum.A, A_NUM);
		equipment[1] = new Equipment(EquipmentEnum.B, B_NUM);
		equipment[2] = new Equipment(EquipmentEnum.C, C_NUM);
	}
	
	/* 判断设备是否空闲
	*	equipmentEnum 设备名
	*/
	public static boolean judgeEquipment(EquipmentEnum equipmentEnum) {
		boolean flag = false;
		switch (equipmentEnum) {
			case A:
				if(equipment[0].getNum() >= 1) {
					flag = true;
				}
				break;
			case B:
				if(equipment[1].getNum() >= 1) {
					flag = true;
				}
				break;
			case C:
				if(equipment[2].getNum() >= 1) {
					flag = true;
				}
				break;
		}
		return flag;
	}
	
	/* 分配设备
	 * equipmentEnum 设备名
	 * time 设备使用时间
	 * id 进程id
	 */
	public static int allocateEquipment(EquipmentEnum equipmentEnum, int time, long id) {
		int n = -1;
		String type="";
		int t ;
		switch (equipmentEnum) {
		case A:
			equipment[0].setNum(equipment[0].getNum()-1);
			for(int i=0;i<2;i++) {
				if(equipment[0].getStatus(i) == 0) {
					equipment[0].setStatus(i, 1);
					equipment[0].setTime(i, time);
					equipment[0].setId(i, id);
					n = i;
					t = n+1;
					type='A'+String.valueOf(t);
					break;
				}
			}
			break;
		case B:
			equipment[1].setNum(equipment[1].getNum()-1);
			for(int i=0;i<3;i++) {
				if(equipment[1].getStatus(i) == 0) {
					equipment[1].setStatus(i, 1);
					equipment[1].setTime(i, time);
					equipment[1].setId(i, id);
					n = i;
					t = n+1;
					type='B'+String.valueOf(t);
					break;
				}
			}
			break;
		case C:
			equipment[2].setNum(equipment[2].getNum()-1);
			for(int i=0;i<3;i++) {
				if(equipment[2].getStatus(i) == 0) {
					equipment[2].setStatus(i, 1);
					equipment[2].setTime(i, time);
					equipment[2].setId(i, id);
					n = i;
					t = n+1;
					type='C'+String.valueOf(t);
					break;
				}
			}
			break;
		default:break;
		}
		System.out.println("设备分配:"+type);
		equipmentController.setEquipmentInfo("忙碌", String.valueOf(id), String.valueOf(time), type);
		return n;
	}
	
	// 回收设备
	public static void callbackEquipment(EquipmentEnum equipmentEnum, int n) {
		String type="";
		int t = n+1;
		switch (equipmentEnum) {
		case A:
			equipment[0].setNum(equipment[0].getNum()+1);
			equipment[0].setStatus(n, 0);
			equipment[0].setId(n, 0);
			type="A";
			break;
		case B:
			equipment[1].setNum(equipment[1].getNum()+1);
			equipment[1].setStatus(n, 0);
			equipment[1].setId(n, 0);
			type="B";
			break;
		case C:
			equipment[2].setNum(equipment[2].getNum()+1);
			equipment[2].setStatus(n, 0);
			equipment[2].setId(n, 0);
			type="C";
			break;
		default:break;
		}
		equipmentController.setEquipmentInfo("空闲", null, null, type+String.valueOf(t));
	}
	
	// 设备倒计时
	public static void run() {
		String type="";
		int t ;
		int time;
		// A:
		for(int i=0; i<A_NUM; i++) {
			if(equipment[0].getStatus(i) == 1) {
				time = equipment[0].getTime(i)-1;
				equipment[0].setTime(i, time);
				t = i + 1;
				type='A'+String.valueOf(t);
				System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!time:"+time);
				equipmentController.setTime(type, String.valueOf(time));
				
			}
		}
		// B:
		for(int i=0; i<B_NUM; i++) {
			if(equipment[1].getStatus(i) == 1) {
				time = equipment[1].getTime(i)-1;
				equipment[1].setTime(i, time);
				t = i + 1;
				type='B'+String.valueOf(t);
				System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!time:"+time);
				equipmentController.setTime(type, String.valueOf(time));
				
			}
		}
		// C:
		for(int i=0; i<C_NUM; i++) {
			if(equipment[2].getStatus(i) == 1) {
				time = equipment[2].getTime(i)-1;
				equipment[2].setTime(i, time);
				t = i + 1;
				type='C'+String.valueOf(t);
				System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!time:"+time);
				equipmentController.setTime(type, String.valueOf(time));
				
			}
		}
	}
	
	// 通过进程id判断该设备是否为空
	public static boolean judgeEquipmentById(Long id) {
		// A:
		for(int i=0; i<A_NUM; i++) {
			if(equipment[0].getId(i) == id && equipment[0].getTime(i) == 0) {
				callbackEquipment(EquipmentEnum.A, i);
				return true;
			}
		}
		// B:
		for(int i=0; i<B_NUM; i++) {
			if(equipment[1].getId(i) == id && equipment[1].getTime(i) == 0) {
				callbackEquipment(EquipmentEnum.B, i);
				return true;
			}
		}
		// C:
		for(int i=0; i<C_NUM; i++) {
			if(equipment[2].getId(i) == id && equipment[2].getTime(i) == 0) {
				callbackEquipment(EquipmentEnum.C, i);
				return true;
			}
		}
		
		return false;
	}
	
	public static void init(EquipmentController e) {
		equipmentController = e;
	}
}