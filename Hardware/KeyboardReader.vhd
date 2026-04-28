library ieee;
use IEEE.std_logic_1164.all;

entity KeyboardReader is
    port(
			Rows: in std_logic_vector(3 downto 0);
			--Tdelay: in std_logic_vector(1 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			TXclk: in std_logic;
			
			TXd: out std_logic;
			Cols: out std_logic_vector(3 downto 0));
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

component RingBuffer is
    port(
			CLK: in std_logic;
			RESET: in std_logic;
			CTS: in std_logic;
			DAV: in std_logic;
			D: in std_logic_vector(3 downto 0);
			
			Wreg: out std_logic;
			Q: out std_logic_vector(3 downto 0);
			DAC: out std_logic);
end component;

component KeyTransmitter is
    port(
			Load: in std_logic;
			D: in std_logic_vector(3 downto 0);
			TXclk: in std_logic;
			RESET: in std_logic;
			
			KBfree: out std_logic;
			TXd: out std_logic);
end component;

signal DAC_out: std_logic;
signal Kval_out: std_logic;
signal K_out: std_logic_vector(3 downto 0);
signal Q_out: std_logic_vector(3 downto 0);
signal KBfree_out: std_logic;
signal Wreg_out: std_logic;

begin
KD: KeyDecode port map (
	 Kack => DAC_out,
	 Rows => Rows,
	 RESET => RESET,
	 Osc => Osc,
	 Cols => Cols,
	 K => K_out,
	 Kval => Kval_out);
	 
RB: RingBuffer port map (
	 CLK => Osc,
	 RESET => RESET,
	 CTS => KBfree_out,
	 DAV => Kval_out,
	 D => K_out,
	 Wreg => Wreg_out,
	 Q => Q_out,
	 DAC => DAC_out);
	 
KT: KeyTransmitter port map (
	 Load => Wreg_out,
	 D => Q_out,
	 TXclk => TXclk,
	 RESET => RESET,
	 KBfree => KBfree_out,
	 TXd => TXd);
	 
end arch_kbreader;
