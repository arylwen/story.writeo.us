/* A simple guild trainer */

#pragma strict_types

/* Base file for skill trainers */
inherit "/lib/skill_raise";
inherit "/std/room";

#include "guild.h"

#include <ss_types.h>

/*
 * Function name: set_up_skills
 * Description:   Initialize the trainer and set the skills we train
 */
void
set_up_skills()
{
    create_skill_raise();
  
    sk_add_train(SS_GUILD_SPECIAL_SKILL, "ability transmutate things ", "alchemy",
	80, 100);
}

void
create_room()
{
    set_short("Guild Trainer");
    set_long("This is the " + GUILD_NAME + " training hall.\n");

    add_exit("start", "north");

    /* configure the trainer */
    set_up_skills();
}

void
init()
{
    ::init();

    /* add the trainer's commands */
    init_skill_raise();
}
