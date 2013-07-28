/*
 * /doc/examples/poison/dagger.c
 *
 * Quis 920618
 *
 * Recode Shiva 990302
 */

inherit "/std/weapon";
#include <ss_types.h>
#include <wa_types.h>
#include <poison_types.h>
#include <files.h>

static int poison_used;

void
create_weapon()
{
    /* Set up the name of the dagger, etc. */

    set_name("dagger");
    set_short("small dagger"); /* Observe, not 'a small dagger' */
    set_long("You percieve a sheen of liquid on the blade.\n");
    set_adj("steel");
    add_adj("small");

    /* Make it fairly ordinary */
    set_hit(12);
    set_pen(10);
    set_wt(W_KNIFE);

    set_dt(W_IMPALE);
    set_hands(W_ANYH);

    /* the poison isn't used upon creation */
    poison_used = 0;

}

/*
 * Function name: remove_poison
 * Description:   Call this to remove the poison from the blade
 */
void
remove_poison()
{
    poison_used = 1;
    set_long("You see nothing special about the dagger.\n");
}

/*
 * We redefine did_hit() in order to actually poison the player.  We will
 * set it up so that the poison wears off the blade after a while.
 */

public varargs int
did_hit(int aid, string hdesc, int phurt, object enemy, int dt, int phit,
    int dam)
{
    object poison;

/*
 * Now we create the poison.  For fun we will make it an anti-mage
 * poison.  After we clone it, we move it to the consuming living, 
 * then call the activating function, start_poison() where we pass
 * the responsible poisoner as argument.
 */
    if (!poison_used && phurt > 0 && !random(5))
    {
        setuid();
        seteuid(getuid());

        poison = clone_object(POISON_OBJECT);
        if( random(2))
        {
            remove_poison();
	}

        poison->move(enemy);
        poison->set_time(500);
        poison->set_interval(60);
        poison->set_strength(40);
        poison->set_damage(({POISON_MANA, 100, POISON_STAT, SS_INT}));
        poison->start_poison(query_wielded());
    }

    return ::did_hit(aid, hdesc, phurt, enemy, dt, phit, dam);
}

/* If the weapon is going to recover, we need to save the poison
 * status.
 */ 
string
query_recover()
{
    return ::query_recover() + "PUSED:" + poison_used;
}

/* When the weapon recovers, we need to restore the poison status */
void
init_recover(string arg)
{
    string str;

    sscanf(arg, "%sPUSED:%d", str, poison_used);

    if (poison_used)
    {
        remove_poison();
    }

    ::init_recover(arg);
}