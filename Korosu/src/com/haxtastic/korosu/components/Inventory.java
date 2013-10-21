package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Inventory extends Component {
	private List<Weapon> 	weapons;
	private List<Ammo>		ammo;
	private Weapon 			weapon;
	
	public Inventory() {
		weapons = new ArrayList<Weapon>();
		weapon = new Weapon(10f, 0.5f, 4f, 8f, "Pistol", "9mm");
		weapons.add(weapon);
		weapons.add(new Weapon(15f, 0.3f, 1f, 9f, "Submachine", "9mm"));
		weapons.add(new Weapon(20f, 0.1f, 1f, 9f, "Colt", "9mm"));
		weapons.add(new Weapon(25f, 0.001f, 1f, 9f, "Turbo", "9mm"));
		weapons.add(new Weapon(15f, 0.5f, 5f, 6f, "Shotgun", "9mm"));
		ammo = new ArrayList<Ammo>();
		ammo.add(new Ammo(1, 25, "9mm"));
		ammo.add(new Ammo(1, 25, "Shells"));
		
	}
	
	public Bullet getBullet() {
		ListIterator<Ammo> iterAmmo = ammo.listIterator();
		Ammo temp  = new Ammo();
		Weapon wTemp = getWeapon();
		while(iterAmmo.hasNext()){
			temp = iterAmmo.next();
			if(temp.bullet == wTemp.ammo)
				break;
		}
		return new Bullet(temp.damage, temp.bullet, temp.width, wTemp.range);
	}
	
	public int cycleWeapon(boolean fresh) {
		ListIterator<Weapon> iterWeapon = weapons.listIterator();
		if(!fresh)
			while(iterWeapon.hasNext()){
				Weapon temp = iterWeapon.next();
				if(weapon.type == temp.type) {
					if(!iterWeapon.hasNext())
						return this.cycleWeapon(true);
					break;
				}
			}
		weapon = iterWeapon.next();
		//System.out.println("SELECTED: " + (selected));
		//System.out.println("SIZE: " + (weapons.size() -1));
		//if(weapons.get(selected).amount == 0)
		//	return this.cycleWeapon();
		//System.out.println("Speed: " + (weapon.speed));
		//System.out.println("Rate of Fire: " + (weapon.rof));
		//System.out.println("Name: " + (weapon.type));
		//System.out.println("Index: " + (weapons.indexOf(weapon)));
		return 1;
	}
	
	public Weapon getWeapon() {
		return weapon;
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
