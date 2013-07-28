/*
 * /doc/examples/mobiles/equipped_npc.c
 * 
 * This simple monster demonstrates how to add equipment to your
 * npcs.  In this case, we add a weapon and a piece of armour.
 */

inherit "/std/monster";

#define WEP "/doc/examples/weapons/knife"
#define ARM "/doc/examples/armours/helmet"

void
create_monster()
{
    set_name("ugluk");
    set_race_name("troll"); 
    set_adj("nasty");
    set_long("It is a very ugly and nasty lookin' troll.\n");

    set_stats(({ 20, 5, 20, 3, 3, 3 }));

    /* We need to make sure the user ids are correct before we
     * attempt to clone anything.
     */
    setuid();
    seteuid(getuid());

    /* Clone the weapon and move it to the monster */
    clone_object(WEP)->move(this_object(), 1);
    /* Wield the weapon */
    command("wield all");

    /* Clone the armour and move it to the monster */
    clone_object(ARM)->move(this_object(), 1);
    /* Wear the armour */
    command("wear all");
}

