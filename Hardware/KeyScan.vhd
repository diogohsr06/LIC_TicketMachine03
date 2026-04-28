library ieee;
use IEEE.std_logic_1164.all;

entity KeyScan is
    port
        (
         Rows: in std_logic_vector(3 downto 0);
         Kscan: in std_logic;
			Osc: in std_logic;
			RESET: in std_logic;
			
         K: out std_logic_vector(3 downto 0);
         Cols: out std_logic_vector(3 downto 0);
         Kpress: out std_logic);
end KeyScan;

architecture arch_KeyScan of KeyScan is
component Counter is
    port(
         CE: in std_logic;
         CLK: in std_logic;
         RESET: in std_logic;
			Q: out std_logic_vector(3 downto 0));
end component;

component Decoder is
    port(
         S: in std_logic_vector(1 downto 0);
         R: out std_logic_vector(3 downto 0));
end component;

component MUX is
    port(
         X: in std_logic_vector(3 downto 0);
         S: in std_logic_vector(1 downto 0);
         R: out std_logic);
end component;

signal Qout: std_logic_vector(3 downto 0);
signal Rout: std_logic_vector(3 downto 0);
signal notKpress: std_logic;
begin
UCOUNTER: Counter port map (
          CE => Kscan,
			 CLK => Osc,
			 RESET => RESET,
			 Q => Qout);
			 
UDEC: Decoder port map (
      S(1) => Qout(3),
		S(0) => Qout(2),
		R => Rout);
		
UMUX: MUX port map (
      X => Rows,
		S(1) => Qout(1),
		S(0) => Qout(0),
		R => notKpress);
		
K <= Qout;
Cols <= not Rout;
Kpress <= not notKpress;

end arch_KeyScan;
