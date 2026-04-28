library ieee;
use IEEE.std_logic_1164.all;

entity KeyDecode is
    port
        (
         Kack: in std_logic;
			--Tdelay: in std_logic_vector(1 downto 0);
			Rows: in std_logic_vector(3 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			
			Cols: out std_logic_vector(3 downto 0);
			K: out std_logic_vector(3 downto 0);
			Kval: out std_logic);
end KeyDecode;
architecture arch_keydecoder of KeyDecode is
component KeyScan is
    port
        (
         Rows: in std_logic_vector(3 downto 0);
         Kscan: in std_logic;
			Osc: in std_logic;
			RESET: in std_logic;
			
         K: out std_logic_vector(3 downto 0);
         Cols: out std_logic_vector(3 downto 0);
         Kpress: out std_logic);
end component;

component KeyControl is
    port
        (
         Kack: in std_logic;
			--Tdelay: in std_logic_vector(1 downto 0);
			Kpress: in std_logic;
			CLK: in std_logic;
			RESET: in std_logic;
			
			Kval: out std_logic;
			Kscan: out std_logic);
end component;

signal kscan_out: std_logic;
signal Kpress_out: std_logic;

begin

KS: KeyScan port map(
    Rows => Rows,
	 Kscan => kscan_out,
	 Osc => Osc,
	 RESET => RESET,
	 K => K,
	 Kpress => Kpress_out,
	 Cols => Cols);
	 
KC: KeyControl port map(
	 Kack => Kack,
	 Kpress => Kpress_out,
	 CLK => Osc,
	 RESET => RESET,
	 Kval => Kval,
	 Kscan => Kscan_out);
	
end arch_keydecoder;