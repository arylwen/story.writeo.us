#include <lib.h>

inherit LIB_DOOR;

static void create() {
    door::create();

    SetSide("north", (["id" : ({ "steel door", "steel door leading north", "door", "north door" }),
        "short" : "a steel door leading north",
        "long" : "This is an imposing, large steel door leading north into the arena.",
        "lockable" : 0 ]) );
    SetSide("south", (["id" : ({ "steel door", "steel door leading south", "south door", "door" }),
        "short" : "a steel door leading south",
        "long" : "This is an imposing, large steel door leading south, out of the arena.",
        "lockable" : 0 ]) );

    SetClosed(0);
    SetLocked(0);
}
void init(){
    ::init();
}
