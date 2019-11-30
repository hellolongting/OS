package equipment.domain;

public class Equipment {
	private EquipmentEnum equipmentType;
	private int num;
	/**
	 * 0表示空闲，1表示占用
	 */
	private int[] status;
	private int[] time;
	private long[] id;
	public Equipment(EquipmentEnum equipmentType,int num) {
		this.equipmentType = equipmentType;
		this.num = num;
		status = new int[num];
		time = new int[num];
		id = new long[num];
		for(int i=0;i<num;i++) {
			this.status[i] = 0;
		}
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	public int getStatus(int i) {
		return status[i];
	}

	public void setStatus(int i,int status) {
		this.status[i] = status;
	}

	public int getTime(int i) {
		return time[i];
	}

	public void setTime(int i,int time) {
		this.time[i] = time;
		System.out.println("time : "+time);
	}
	
	public long getId(int i) {
		return id[i];
	}

	public void setId(int i,long id) {
		this.id[i] = id;
	}

}
