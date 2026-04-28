library ieee;
use IEEE.std_logic_1164.all;

entity KeyboardReader is
    port(
			Rows: in std_logic_vector(3 downto 0);
			--Tdelay: in std_logic_vector(1 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			Kack: in std_logic;
			
			Cols: out std_logic_vector(3 downto 0);
			K: out std_logic_vector(3 downto 0);
			Kval: out std_logic);
        
end KeyboardReader;

architecture arch_kbreader of KeyboardReader is
component KeyDecode is
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
end component;



begin
KD: KeyDecode port map(
	 Kack => Kack,
	 Rows => Rows,
	 RESET => RESET,
	 Osc => Osc,
	 Cols => Cols,
	 K => K,
	 Kval => Kval);
	 
end arch_kbreader;
