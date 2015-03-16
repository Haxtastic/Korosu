package com.haxtastic.korosu.components;

import com.artemis.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Inventory extends Component {
	private List<Weapon> 	weapons;
	private List<Ammo>		ammunition;
	private Weapon 			weapon;
	private Ammo			ammo;
	
	public Inventory() {
		weapons = new ArrayList<Weapon>();
		weapon = new Weapon(5f, 0.25f, 4f, 8f, "Pistol", "9mm");
		weapons.add(weapon);
		
		ammunition = new ArrayList<Ammo>();
		ammo = new Ammo(1, -1, "9mm");
		ammunition.add(ammo);
	}
	
	public Bullet getBullet() {
		if(weapon.ammo == ammo.bullet) {
			if(ammo.amount != -1)
				ammo.amount-=1;
			return new Bullet(ammo.damage, ammo.bullet, ammo.width, weapon.range);
		}
		return null;
	}
	
	public boolean setAmmo() {
		ListIterator<Ammo> iterAmmo = ammunition.listIterator();
		while(iterAmmo.hasNext()){
			Ammo temp  = iterAmmo.next();
			if(temp.bullet == weapon.ammo && (temp.amount > 0 || temp.amount == -1)){
				ammo = temp;
				return true;
			}
		}
		return false;
	}
	
	public int cycleWeapon(boolean fresh) {
		ListIterator<Weapon> iterWeapon = null;
		if(fresh)
			iterWeapon = weapons.listIterator();
		else
			iterWeapon = weapons.listIterator(weapons.indexOf(weapon)+1);
		
		if(!iterWeapon.hasNext() && !fresh)
			return cycleWeapon(true);
		weapon = iterWeapon.next();
		if(!setAmmo())
			return cycleWeapon(false);
		return 1;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public Ammo getAmmo() {
		return ammo;
	}
	
	public int addWeapon(Weapon wea) {
		int index = weapons.indexOf(wea);
		if(index == -1) {
			weapons.add(wea);
			return 1;
		}
		return weapons.get(index).amount +=1;
	}
	
	public int addAmmo(Ammo am) {
		int index = ammunition.indexOf(am);
		if(index == -1) {
			ammunition.add(am);
			return am.amount;
		}
		Ammo temp = ammunition.get(index);
		temp.amount = temp.amount + am.amount;
		return temp.amount;
	}
	
	public void clean() {
		weapons.clear();
		ammunition.clear();
	}
}
