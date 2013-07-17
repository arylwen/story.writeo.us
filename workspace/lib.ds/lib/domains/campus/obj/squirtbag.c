/*    /domains/Examples/etc/bag.c
 *    from the Dead Souls LPC Library
 *    a sample bag object
 *    created by Descartes of Borg 950529
 */

#include <lib.h>

inherit LIB_STORAGE;

void create() {
    ::create();
    SetKeyName("bag");
    SetId( ({ "bag" }) );
    SetAdjectives( ({ "small", "cloth", "a" }) );
    SetShort("a small cloth bag");
    SetLong("It is a simple cloth bag used to hold things. It has a cute Virtual Campus "+
      "logo on it.");
    SetInventory(([
        "/domains/campus/weap/waterpistol" : 1,
      ]));
    SetMass(10);
    SetDollarCost(1);
    SetMaxCarry(50);
}
void init(){
    ::init();
}
