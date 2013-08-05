/*    /secure/lib/connect.c
 *    from the Nightmare IVr2 Object Library
 *    the object a user connects to before determining their real body
 *    created by Descartes of Borg 951103
 *    Version: @(#) connect.c 1.2@(#)
 *    Last Modified: 96/05/28
 */

#include <lib.h>
#include <rooms.h>
#include <news.h>
#include <flags.h>
#include <cfg.h>
#include <config.h>
#include <daemons.h>
#include <message_class.h>
#include "include/connect.h"

inherit LIB_PAGER;

static private int CrackCount, NetDead; 
static private string name, Name, Terminal, raza, TrueName, trabajo; 
static private string Passwort, Gendre, CapNamen, E_Mail; 
static private object Player, pile; 
static private mapping LastError;
static private int *Screen;
static private int rescue, yescre;
static private string *ip_permitted = ({});

//Change this to 1 to enable color codes in /news/welcome
//then update the connect.c file.
//Note that it may look weird or broken on some people's terminals.
static private int AllowColorSplash = 0;

int eventCheckPending(string str);
void eventCompleteChar();
void InputCre(string str);
static void cmdPickClass(string args);
static void cmdListClasses();
static void cmdHelpClass(string args);
static void InputClass(string str);
static void eventSelectClass();

int CheckIp(){
    string my_ip = query_ip_number();
    int ips = 0;
    if(!ip_permitted) ip_permitted = ({});
    ip_permitted += PLAYERS_D->GetAdminIPs();
    if(!SAME_IP_MAX) return 1;
    foreach(object jugador in users()){
        if(query_ip_number(jugador) == my_ip) ips++;
    }
    if(ips >= SAME_IP_MAX && member_array(my_ip, ip_permitted) == -1) return 0;
    else return 1;
}

static void create() {
    CrackCount = 0;
    Name = "";
    Player = 0;
    NetDead = 0;
    ip_permitted = explode(read_file(CFG_IP_UNRESTRICT),"\n");
}

varargs static void logon(mixed foo) {
    string welcome = "Connected to "+mud_name()+", an LP mud";
    if(sizeof(architecture())) welcome += " running on "+architecture()+".\n";
    else welcome += ".\n";

    call_out((: eventTimeout :), LOGON_TIMEOUT);
    if(mud_name() == "Dead Souls" || mud_name() == "Dead Souls Demo" ){
        welcome += read_file(DS_WELCOME) || "Welcome to " + mud_name() + "!";
    }
    else {
        welcome += read_file(NEWS_WELCOME) || "Welcome to " + mud_name() + "!";
    }
    if(AllowColorSplash){
        receive(terminal_colour(welcome,TERMINAL_D->query_term_info("ansi")));
    }
    else {
        receive(welcome);
    }    
    receive("\n" + center("Driver: " + version() + "               Mudlib: "+
        mudlib() + " " + mudlib_version()) + "\n");
    if(CheckIp()){ 
        receive("\nWhat name do you wish? \n");
        input_to((: InputName :), I_NOESC); 
    }
    else {
        receive("\nToo many users from your site are currently logged on.\n");
        receive("Please try again later.\n");
        Destruct();
        return;
    }
} 

static void InputName(string namen, string kill_me) {
    string temp_name, news;
    mixed tmp;
    name = namen;

    if( !name || name == "" ) {
        if( kill_me ) {
            receive("\nPerhaps try another time then?\n");
            Destruct();
            return;
        }
        else {
            receive("\nYou must enter a name in order to join!\n");
            receive("\nWhat name do you wish? \n");
            input_to((: InputName :), I_NOESC, "kill me");
            return;
        }
    }
    if(sscanf(name,"%s_rescue",temp_name) == 1) {
        name = temp_name;
        rescue = 1;
    }
    Name = convert_name(name);
    name = capitalize(name);
    if( (int)master()->is_locked() ) {
        news = read_file(NEWS_LOCKED) || "No reason logged.";
        receive("\n" + mud_name() + " is locked.  Reason:\n" + news + "\n");
        if( CanLogin() ) receive("\n        >>> Access allowed <<<\n");
        else {
            receive("\n        >>> Access denied <<<\n");
            Destruct();
            return;
        }
    }
    if( !user_exists(Name) ) {
        eventCreatePlayer(name);
        return;
    }
    if( !((int)BANISH_D->eventConnect(Name, query_ip_number())) ) {
        news = read_file(NEWS_BANISHED) || "You are not allowed here.\n";
        receive("\n" + news + "\n");
        Destruct();
        return; 
    }
    if( (tmp = (mixed)CHARACTER_D->eventConnect(Name)) != 1 ) {
        if( tmp ) receive(tmp + "\n");
        else receive("One of your characters was recently logged in.\n"
              "You must wait a little longer before logging in.\n");
        Destruct();
        return;
    }

    if( find_player(Name) ) NetDead = 1;

    /*if(Name != "guest"){
        receive("Password: \n");
        input_to((: InputPassword :), I_NOECHO | I_NOESC, name);
    }
    else InputPassword("guest","Guest");*/
	InputPassword("","Name");
} 

static void InputPassword(string pass, string cap) {
    string control;

    if(Name != "guest"){

        /*if( !pass || pass == "" ) {
            receive("\nYou must enter a password.  Please try again later.\n");
            Destruct();
            return;
        }*/
        if(!cap || cap == "") {
            receive("\nAn unusual error has occurred.  Please try again.\n");
            Destruct();
            return;
        }

        control = PLAYERS_D->GetPlayerData(Name,"Password");
        /*if( control != crypt(pass, control) ) {
            receive("\nInvalid password.\n");
            if( ++CrackCount > MAX_PASSWORD_TRIES ) {
                receive("\nNo more attempts allowed\n");
                log_file("security", "Maximum password tries exceeded by " +
                  Name + " from " + query_ip_number() + "\n");
                filter(users(), (: archp :))->eventPrint("System %^MAGENTA%^"
                  "<error>%^RESET%^ Maximum password "
                  "attempts exceeded by " + cap +
                  " from " + query_ip_number() + ".\n",
                  MSG_ERROR);
                Destruct();
                return;
            }
            receive("Password: \n");
            input_to( (: InputPassword :), I_NOECHO | I_NOESC, cap);
            return;
        }*/

        if(rescue) unguarded( (: wipe_inv(name) :) );
    }

    Player = (object)master()->player_object(Name);
    if( !Player ) {
        receive("\nIt seems some work is being done right now, try later.\n");
        Destruct();
        return;
    }
    PLAYERS_D->AddPlayerInfo(Player);
    if( !(string)Player->GetRace() || (string)Player->GetRace() == "blob"
      || Name == "guest" ) {
        Player->SetRace("human");
    }

	//races::create();
	
    call_out( (: eventCheckPending, lower_case(Name) :), 3);
    if( !NetDead ) eventEnterGame();
    else eventReEnterGame(cap);
}

static void eventCreatePlayer(string cap) {
    if( !((int)BANISH_D->valid_name(lower_case(Name))) ) {
        receive(capitalize(cap) + " is not a valid name.\n");
        receive(mud_name() + " requires that all names meet the following "
          "requirements:\n");
        receive("\tAll characters must be:\n");
        receive("\t\tA-Z\n\t\ta-z\n\t\t'\n\t\t-\n");
        receive("\tMinimum length: " + MIN_USER_NAME_LENGTH + "\n");
        receive("\tMaximum length: " + MAX_USER_NAME_LENGTH + "\n");
        receive("\nPlease enter a new name: \n");
        input_to( (: InputName :), I_NOESC );
        return;
    }
    if( !((int)BANISH_D->eventConnect(Name, query_ip_number())) ) {
        string news;

        news = read_file(NEWS_REGISTER) || "No registration info.\n";
        receive(news);
        Destruct();
        return;
    }
    receive("Do you really wish to be known as " + cap + "? (y/n) \n");
    input_to((: ConfirmName :), I_NOESC, cap);
}

static void AgeCheck(string ans, string cap) {
    if( !ans || ans == "" || lower_case(ans)[0..0] != "y" ) {
        receive("\nSorry. You are not old enough to play here.\n");
        Destruct();
        return;
    }
    log_file("new_players", Name + " from " + query_ip_number() + " (" +
      ctime(time()) + ")\n");
    receive("\nCreate a password of at least 5 letters: \n");
    input_to((: CreatePassword :), I_NOECHO | I_NOESC, cap);
} 

static void ConfirmName(string ans, string cap) {
    if( !ans || ans == "" || lower_case(ans)[0..0] != "y" ) {
        receive("\nOk, then enter the name you really want: \n");
        input_to( (: InputName :), I_NOESC );
        return;
    }
    receive("\nAre you 13 years of age or older? (y/n) \n");
    input_to((: AgeCheck :), I_NOESC, cap);
}

static void eventReEnterGame(string cap) {
    if( interactive(Player) ) {
        receive("\nAn interactive copy of you currently exists.\n");
        receive("Do you wish to take over this copy? (y/n) \n");
        input_to((: ConfirmReconnect :), I_NOESC, cap);
        return;
    }
    if( exec(Player, this_object()) ) {
        if(rescue) 
            if(sizeof(deep_inventory(Player))) deep_inventory(Player)->eventMove(ROOM_FURNACE);
        log_file("enter", cap + " (exec): " + ctime(time()) + "\n"); 
        Player->eventReconnect();
        Player->RemoveProperty("afk");
        if(creatorp(Player)) 
            Player->AddChannel((CHAT_D->GetSystemChannels() - ({"muds"})));
        SNOOP_D->ReportReconnect(Player->GetKeyName());
    }
    receive("\nProblem reconnecting.\n");
    Destruct();
}

static void ConfirmReconnect(string ans, string cap) {
    object tmp; 

    if( !stringp(ans) || ans == "" || lower_case(ans)[0..0] != "y" ) {
        receive("\nThen please try again later!\n");
        Destruct();
        return;
    }
    if(rescue)
        if(sizeof(deep_inventory(Player))) deep_inventory(Player)->eventMove(ROOM_FURNACE);
    log_file("enter", cap + " (reconnect) from " + query_ip_number() + " (" +
      ctime(time()) + ")\n");
    exec(tmp = new(LIB_PLAYER), Player); 
    exec(Player, this_object()); 
    destruct(tmp);
    Player->eventPrint("\nAllowing login.\n", MSG_SYSTEM);
    Destruct();
}

static int CanLogin() {
    string group;

    if( (int)BANISH_D->GetGuest(Name) ) return 1;
    foreach(group in LOCKED_ACCESS_ALLOWED)
    if( member_group(Name, group) ) return 1;
    return 0;
}

static private void eventEnterGame() { 
    if(!exec(Player, this_object())) { 
        debug("problem connecting");
        receive("\nProblem connecting.\n"); 
        Player->eventDestruct();
        destruct(this_object());
        return; 
    } 
    if( Terminal ) Player->SetTerminal(Terminal);
    if( Screen ) Player->SetScreen(Screen[0], Screen[1]);
	    write_file("log_gab", "/secure/lib/connect.c eventEnterGame before player.setup\n");
    Player->Setup();
    if(creatorp(Player)){  
        string profile = user_path(Player->GetKeyName())+".profile";
        if(file_exists(profile)){
            Player->eventForce("source "+profile);
        }
    }
    Player->RemoveProperty("afk");
    if(creatorp(Player)) 
        Player->AddChannel(((CHAT_D->GetSystemChannels()) - ({"muds"})));
    SNOOP_D->CheckBot(Player->GetKeyName());
    call_out( (: destruct(this_object()) :), 10);

} 

static void CreatePassword(string pass, string cap) {
    if( strlen(pass) < 3) {
        receive("\nYour password must be at least 5 letters in length.\n");
        receive("Please choose another password: \n");
        input_to( (: CreatePassword :), I_NOECHO | I_NOESC, cap);
        return;
    }
    receive("\nPlease confirm your password: \n");
    input_to( (: ConfirmPassword :), I_NOECHO | I_NOESC, cap, pass);
}

static void ConfirmPassword(string control, string cap, string pass) {
    if( control == pass ) {
        Player = (object)master()->player_object(Name);
        pass = crypt(pass, 0);
        Passwort = pass;
        receive("\n\nPlease choose an interesting gender (male, female, neutral, or none): \n");
        input_to((: InputGender :), I_NOESC, cap);
        return; 
    } 
    receive("\nPassword entries do not match, re-choose password: \n");
    input_to((: CreatePassword :), I_NOECHO | I_NOESC, cap);
    return; 
} 

static void InputGender(string str, string cap) {
    if( str != "male" && str != "female" 
      && str != "neuter" && str != "neutral" && str != "none") { 
        receive("\nCute, but pretend to be male, female, neutral, or none instead.\n");
        receive("Gender: ");
        input_to((: InputGender :), I_NOESC, cap);
        return;
    }
    if(str == "none") str = "neuter";
    Gendre = str;
    receive("\nYou may format " + cap + " to appear however you wish using "
      "spaces, ', or -.\nEnter a display name (default: " + cap +"): \n");
    input_to((: InputCapName :), I_NOESC, cap);
}

static void InputCapName(string name, string cap) {
    if( !name || name == "" ) name = cap;
    if( !((int)BANISH_D->valid_cap_name(name, Name)) ) {
        receive("\nThat was not a valid name format, choose again: \n");
        input_to((: InputCapName :), I_NOESC, cap);
        return;
    }
    cap = capitalize(name);
    CapNamen = cap;
    receive("\nFor security reasons, " + mud_name() + " requires a valid "
      "email.\n"
      "Email: \n");
    input_to((: InputEmail :), I_NOESC);
} 

static void InputEmail(string email) {
    string user, host; 

    if( !email || sscanf(email, "%s@%s", user, host) != 2 ) {
        receive("\nThat is not a valid email address.\n");
        receive("Please enter a valid email address: \n");
        input_to( (: InputEmail :), I_NOESC );
        return; 
    } 
    E_Mail = email;
    receive("\nIf you do not mind, please enter your real name (optional): \n");
    input_to( (: InputRealName :), I_NOESC );
}

static void InputRealName(string rname) {
    if( !rname || rname == "" ) rname = "Unknown";
    TrueName = rname;
    if(!HUMANS_ONLY){
        receive("\n\nYou must now pick a race.\n");
        receive("Picking a race influences what physical traits your character "
          "will have.\n");
        receive("\nYou may issue the following commands:\n");
        receive("\tlist - lists all races from which you can choose\n");
        receive("\thelp - get help on what races mean\n");
        receive("\thelp RACE - (e.g. \"help human\") gives you information on "
          "a race\n");
        receive("\tpick RACE - pick a particular race for yourself\n");
        receive("\nRace: \n");
        input_to((: InputRace :), I_NOESC);
    }
    else InputRace("pick human");
}

static void InputRace(string str) {
    string cmd, args;

    if( str == "" || !str ) {
        receive("\nRace: ");
        input_to((: InputRace :), I_NOESC);
        return;
    }
    if( sscanf(str, "%s %s", cmd, args) != 2 ) {
        cmd = str;
        args = 0;
    }
    switch(cmd) {
    case "list":
        cmdList();
        return;

    case "help":
        cmdHelp(args);
        return;

    case "pick":
        cmdPick(args);
        return;

    default:
        receive("\nInvalid command.\nRace: \n");
        input_to((: InputRace :), I_NOESC);
        return;
    }
}

static void cmdHelp(string args) {
    function f;
    string array races = (string array)RACES_D->GetRaces(1);
    string help;

    f = function(string str) {
        receive("\nRace: \n");
        input_to((: InputRace :), I_NOESC);
    };
    if( !args ) {
        help = read_file(DIR_PLAYER_HELP + "/races") ||
        "Error reading help file.\n";
        eventPage(explode(help, "\n"), MSG_HELP, f);
        return;
    }
    if( member_array(args = lower_case(args), races) == -1 ) {
        receive("No such race exists.\n\nRace: \n");
        input_to((: InputRace :), I_NOESC);
        return;
    }
    help = (string)RACES_D->GetHelp(args);
    eventPage(explode(help, "\n"), MSG_HELP, f);
}

static void cmdList() {
    string list;

    list = format_page((string array)RACES_D->GetRaces(1), 5, 0);
    receive(list);
    receive("\nRace: \n");
    input_to((: InputRace :), I_NOESC);
}

static void cmdPick(string args) {
    if( !args || args == "" ) {
        receive("You must specify a race to pick.\n\nRace: \n");
        input_to((: InputRace :), I_NOESC);
        return;
    }
    if( member_array(args = lower_case(args),
        (string array)RACES_D->GetRaces(1)) == -1 ) {
        receive("No such race.\nRace: \n");
        input_to((: InputRace :), I_NOESC);
        return;
    }
    raza = args;
    if(!AUTO_WIZ){
        if(CLASS_SELECTION){
            eventSelectClass();
            return;
        }
        eventCompleteChar();
    }
    else {
        receive("This mud has enabled AUTO_WIZ.\n");
        receive("This means that if you wish, you may be automatically \n");
        receive("promoted to creator status. \n\n");
        receive("If you want to be a player, enter: player\n");
        receive("If you want to be a creator, enter: creator\n");
        receive("What is your choice?\n");
        input_to((: InputCre :), I_NOESC);
    }
}

static void eventSelectClass(){
    receive("\n\nYou must now pick a class.\n");
    receive("Picking a class influences what skills your character "
      "will have.\n");
    receive("\nYou may issue the following commands:\n");
    receive("\tlist - lists all classes from which you can choose\n");
    receive("\thelp - get help on what classes mean\n");
    receive("\thelp CLASS - (e.g. \"help explorer\") gives you information on "
      "a class\n");
    receive("\tpick CLASS - pick a particular class for yourself\n");
    receive("\nClass: \n");
    input_to((: InputClass :), I_NOESC);
}

static void InputClass(string str){
    string cmd, args;

    if( str == "" || !str ) {
        receive("\nClass: ");
        input_to((: InputClass :), I_NOESC);
        return;
    }
    if( sscanf(str, "%s %s", cmd, args) != 2 ) {
        cmd = str;
        args = 0;
    }
    switch(cmd) {
    case "list":
        cmdListClasses();
        return;

    case "help":
        cmdHelpClass(args);
        return;

    case "pick":
        cmdPickClass(args);
        return;

    default:
        receive("\nInvalid command.\nClass: \n");
        input_to((: InputClass :), I_NOESC);
        return;
    }
}

static void cmdHelpClass(string args){
    function f;
    string array Classes = (string array)CLASSES_D->GetClasses(1);
    string help;

    f = function(string str) {
        receive("\nClass: \n");
        input_to((: InputClass :), I_NOESC);
    };
    if( !args ) {
        help = HELP_D->GetHelp("player documents classes") ||
        "Error reading help file.\n";
        eventPage(explode(help, "\n"), MSG_HELP, f);
        return;
    }
    if( member_array(args = lower_case(args), Classes) == -1 ) {
        receive("No such Class exists.\n\nClass: \n");
        input_to((: InputClass :), I_NOESC);
        return;
    }
    help = (string)HELP_D->GetHelp("classes "+args);
    eventPage(explode(help, "\n"), MSG_HELP, f);
}

static void cmdListClasses() {
    string list;

    list = format_page((string array)CLASSES_D->GetClasses(1), 5);
    receive(list);
    receive("\nClass: \n");
    input_to((: InputClass :), I_NOESC);
}

static void cmdPickClass(string args) {
    if( !args || args == "" ) {
        receive("You must specify a class to pick.\n\nClass: \n");
        input_to((: InputClass :), I_NOESC);
        return;
    }
    if( member_array(args = lower_case(args),
        (string array)CLASSES_D->GetClasses(1)) == -1 ) {
        receive("No such Class.\nClass: \n");
        input_to((: InputClass :), I_NOESC);
        return;
    }
    trabajo = args;
    eventCompleteChar();
}

int eventCre(string str){
    mixed mixt;
    if(yescre && AUTO_WIZ == 1){
        mixt = load_object("/secure/cmds/admins/encre")->cmd(str);
		write_file("log_gab", "/secure/lib/connect.c eventCre result of encre "+mixt+"\n");
    }
    destruct(this_object());
    return 1;
}

int eventCheckPending(string str){
    mixed mixt;
    if(member_array(lower_case(str),PLAYERS_D->GetPendingEncres()) != -1){
        PLAYERS_D->RemovePendingEncre(lower_case(str));
        mixt = load_object("/secure/cmds/admins/encre")->cmd(str);
    }
    else if(member_array(lower_case(str),PLAYERS_D->GetPendingDecres()) != -1){
        PLAYERS_D->RemovePendingDecre(lower_case(str));
        mixt = load_object("/secure/cmds/admins/decre")->cmd(str);
    }
    destruct(this_object());
    return 1;
}

void InputCre(string str){
    if(!str || str == "" || !sizeof(str) || !stringp(str)) str = "foo";
    if(str != "creator" && str != "player"){
        receive("\nInvalid choice.\n");
        receive("If you want to be a player, enter: player\n");
        receive("If you want to be a creator, enter: creator\n");
        receive("What is your choice?\n");
        input_to((: InputCre :), I_NOESC);
        return;
    }
    if(str == "creator") yescre = 1;
    eventCompleteChar();
}

void eventCompleteChar(){
    write_file("log_gab", "connect.c start complete char\n");
    Player->SetRace(raza);
    Player->SetProperty("brand_spanking_new",1);
    Player->AddCurrency("silver",random(100)+57);
    Player->SetRace(raza);
    Player->SetPassword(Passwort);
    Player->SetGender(Gendre);
    Player->SetCapName(CapNamen);
    Player->SetEmail(E_Mail);
    Player->SetRealName(TrueName);    
    Player->SetLanguage("Common",100);    
    Player->SetDefaultLanguage("Common");   
    if(trabajo) Player->ChangeClass(trabajo);
    this_player()->SetWimpy(20);
    this_player()->SetTerminal("ansi");
	    write_file("log_gab", "connect.c before add player info\n");
    PLAYERS_D->AddPlayerInfo(Name);
	    write_file("log_gab", "connect.c after add player info\n");
    call_out( (: eventCre, Name :), 3);
 	    write_file("log_gab", "before enter game\n");
    eventEnterGame();
	//the callout is for the use to get a chance to enter the game
	   //eventCre(Name);

} 

static void eventTimeout() {
    if( !interactive(this_object()) ) {
        Destruct();
    }
    else if( query_idle(this_object()) > LOGON_TIMEOUT ) {
        receive("\nLogin timed out.\n");
        Destruct();
    }
    else call_out( (: eventTimeout :), LOGON_TIMEOUT );
}

varargs int eventPrint(string msg, mixed cl, mixed arg3) {
    if( !cl || !intp(cl) ) return 0;
    if( cl & MSG_NOWRAP ) receive(strip_colours(msg));
    else receive(wrap(strip_colours(msg), GetScreen()[0]));
    return 1;
}

static private void Destruct() {
    if( Player && !NetDead ) destruct(Player);
    remove_call_out();
    destruct(this_object());
}

void eventDestruct() {
    Destruct();
}

string GetKeyName() {
    if(!interactive(this_object())) return 0;
    else if(Name) return Name;
    else return "";
}

string GetCapName() {
    string tmp;

    tmp = GetKeyName();
    return (tmp ? capitalize(tmp) : "");
}

static void terminal_type(string str) {
    if( !stringp(str) ) return;
    else Terminal = lower_case(str);
}

static void window_size(int width, int height) {
    Screen = ({ width, height });
}

static void CreateGuest() {
    receive("Enter the name you wish to use: \n");
    input_to( (: GetGuestName :), I_NOESC );
}

static void GetGuestName(string nom) {
    object ob;

    return;
    if( !nom || nom == "" || user_exists(convert_name(nom)) ) {
        receive("Illegal name choice, try again: ");
        return;
    }
    ob->SetCapName(nom);
    exec(ob, this_object());
    ob->Setup();
}

int *GetScreen() {
    if( Screen ) return Screen;
    else return ({ 79, 25 });
}

void SetLastError(mapping m) {
    if (previous_object() != master()) return;
    LastError = m;
}

mapping GetLastError() {
    string caller = base_name(previous_object());
    if( caller == "/secure/cmds/creator/dbxwhere" ||
      caller  == "/secure/cmds/player/dbxframe" ) return LastError;
    error("Privilege Violation: " + caller);
}
