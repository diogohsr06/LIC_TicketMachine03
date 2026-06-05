library ieee;
use IEEE.std_logic_1164.all;

entity KeyboardReader is
    port(
			Rows: in std_logic_vector(3 downto 0);
			Tdelay: in std_logic_vector(1 downto 0);
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
			Tdelay: in std_logic_vector(1 downto 0);
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
			Din: in std_logic_vector(3 downto 0);
			
			Wreg: out std_logic;
			Dout: out std_logic_vector(3 downto 0);
			DAC: out std_logic);
end component;

component KeyTransmitter is
    port(
			clk: in std_logic;
			Load: in std_logic;
			D: in std_logic_vector(3 downto 0);
			TX_clk: in std_logic;
			RESET: in std_logic;
			
			KBfree: out std_logic;
			TX_D: out std_logic);
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
	 Tdelay => Tdelay,
	 Osc => Osc,
	 Cols => Cols,
	 K => K_out,
	 Kval => Kval_out);
	 
RB: RingBuffer port map (
	 CLK => Osc,
	 RESET => RESET,
	 CTS => KBfree_out,
	 DAV => Kval_out,
	 Din => K_out,
	 Wreg => Wreg_out,
	 Dout => Q_out,
	 DAC => DAC_out);
	 
KT: KeyTransmitter port map (
	 clk => Osc,
	 Load => Wreg_out,
	 D => Q_out,
	 TX_clk => TXclk,
	 RESET => RESET,
	 KBfree => KBfree_out,
	 TX_D => TXd);
	 
end arch_kbreader;