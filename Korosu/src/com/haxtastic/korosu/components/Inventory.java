package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Inventory extends Component {
	private List<Weapon> 	weapons;
	private List<Ammo>		ammo;
	private int 			selected = 0;
	
	public Inventory() {
		weapons = new ArrayList<Weapon>();
		weapons.add(new Weapon(5f, 0.5f, 1f, 9f, "Pistol", "9mm"));
		weapons.add(new Weapon(5f, 0.5f, 1f, 9f, "Submachine", "9mm"));
		ammo = new ArrayList<Ammo>();
		ammo.add(new Ammo(25, "9mm"));
	}
	
	public int cycleWeapon() {
		ListIterator<Weapon> iterWeapon = weapons.listIterator(selected);
		selected = iterWeapon.nextIndex();
		if(weapons.get(selected).amount == 0)
			return this.cycleWeapon();
		if(selected == weapons.size()) {
			selected = 0;
		}
		return 1;
	}
	
	public Weapon getWeapon() {
		return weapons.get(selected);
	}
	
	public int addWeapon(Weapon wea) {
		int index = weapons.indexOf(wea);
		if(index == -1) {
			return -1;
		}
		return weapons.get(index).amount +=1;
	}
	
	public int addAmmo(Ammo am) {
		int index = ammo.indexOf(am);
		if(index == -1) {
			return -1;
		}
		Ammo temp = ammo.get(index);
		temp.amount = temp.amount + am.amount;
		return temp.amount;
	}
	
	public void clean() {
		weapons.clear();
		ammo.clear();
	}
}
