library ieee;
use IEEE.std_logic_1164.all;

entity TicketMachine is
    port
        (
			Rows: in std_logic_vector(3 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			CT: in std_logic;
			
			Cols: out std_logic_vector(3 downto 0);
			D9: out std_logic;
			D: out std_logic_vector(8 downto 0);
			HEX0, HEX1, HEX2, HEX3, HEX4, HEX5: out STD_LOGIC_VECTOR(7 downto 0));
end TicketMachine;

architecture arch_TM of TicketMachine is
component KeyboardReader is
    port(
			Rows: in std_logic_vector(3 downto 0);
			--Tdelay: in std_logic_vector(1 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			Kack: in std_logic;
			Cols: out std_logic_vector(3 downto 0);
			K: out std_logic_vector(3 downto 0);
			Kval: out std_logic);
end component;

component UsbPort IS 
	PORT
	(
		inputPort:  IN  STD_LOGIC_VECTOR(7 DOWNTO 0);
		outputPort :  OUT  STD_LOGIC_VECTOR(7 DOWNTO 0)
	);
END component;

component PELCD is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			D9: out std_logic;
			D: out std_logic_vector(8 downto 0));
end component;

component TICKET_DISPENSER is
	port ( RT, Prt, CollectTicket: in STD_LOGIC;
			 O, D: in STD_LOGIC_VECTOR(3 downto 0);
			 Fn: out STD_LOGIC;
			 HEX0, HEX1, HEX2, HEX3, HEX4, HEX5: out STD_LOGIC_VECTOR(7 downto 0) );
end component;

component PETD is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			D9: out std_logic;
			D: out std_logic_vector(8 downto 0));
end component;

signal CLK_out: std_logic;

signal Kack_out: std_logic; 
signal Kval_out: std_logic;
signal K_out: std_logic_vector(3 downto 0);

signal O1: std_logic;
signal O2: std_logic;
signal O0: std_logic;
signal O3: std_logic;

signal D9_out: std_logic;
signal D_out: std_logic_vector(8 downto 0);

signal Done: std_logic;
signal Output_usb: std_logic_vector(7 downto 0);

begin

KR: KeyboardReader port map(
	 Rows => Rows,
	 RESET => RESET,
	 Osc => Osc,
	 Kack => O0,
	 Cols => Cols,
	 K => K_out,
	 Kval => Kval_out);
	 
SRLCD: PELCD port map(
	 SDX => O0,
	 SCLK => O1,
	 SS => O2,
	 RESET => RESET,
	 D9 => D9,
	 D => D);
	 
SRTD: PETD port map(
		SDX => O0,
		SCLK => O1,
		SS => O3,
		RESET => RESET,
		D9 => D9_out,
		D => D_out);
		
TD: TICKET_DISPENSER port map(
	 RT => D_out(0),
	 Prt => D9_out,
	 CollectTicket => CT,
	 O(0) => D_out(1),
	 O(1) => D_out(2),
	 O(2) => D_out(3),
	 O(3) => D_out(4),
	 D(0) => D_out(5),
	 D(1) => D_out(6),
	 D(2) => D_out(7),
	 D(3) => D_out(8),
	 Fn => Done,
	 HEX0 => HEX0,
	 HEX1 => HEX1,
	 HEX2 => HEX2,
	 HEX3 => HEX3,
	 HEX4 => HEX4,
	 HEX5 => HEX5);
	 
UUSBPORT: UsbPort port map(
			 inputPort(0) => K_out(0),
			 inputPort(1) => K_out(1),
			 inputPort(2) => K_out(2),
			 inputPort(3) => K_out(3),
			 inputPort(4) => Kval_out,
			 inputPort(5) => Done, --Sem certeza/Temporario
			 inputPort(6) => '0',
			 inputPort(7) => '0',
			 outputPort => Output_usb);
			 
O0 <= Output_usb(0);
O1 <= Output_usb(1);
O2 <= Output_usb(2);
O3 <= Output_usb(3);

end arch_TM;
	 