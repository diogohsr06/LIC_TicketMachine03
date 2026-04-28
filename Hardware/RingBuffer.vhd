library ieee;
use IEEE.std_logic_1164.all;

entity RingBuffer is
    port(
			CLK: in std_logic;
			RESET: in std_logic;
			CTS: in std_logic;
			DAV: in std_logic;
			D: in std_logic_vector(3 downto 0);
			
			Wreg: out std_logic;
			Q: out std_logic_vector(3 downto 0);
			DAC: out std_logic);
end RingBuffer;

architecture arch_RB of RingBuffer is
component RBC is
    port(
			DAV: in std_logic;
			CTS: in std_logic;
			full: in std_logic;
			empty: in std_logic;
			
			Wr: out std_logic;
			selPG: out std_logic;
			incPut: out std_logic;
			incGet: out std_logic;
			Wreg: out std_logic;
			DAC: out std_logic);
end component;

component MAC is
    port(
			CLK: in std_logic;
			RESET: in std_logic;
			putget: in std_logic;
			incPut: in std_logic;
			incGet: in std_logic;
			
			A: out std_logic_vector(3 downto 0);
			full: out std_logic;
			empty: out std_logic);
			
end component;

component RBRAM is
	generic(
		ADDRESS_WIDTH : natural := 4;
		DATA_WIDTH : natural := 4
	);
   port(
			CLK: in std_logic;
			A: in std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
			Din: in std_logic_vector(DATA_WIDTH - 1 downto 0);
			wr: in std_logic;
			
			Dout: out std_logic_vector(DATA_WIDTH - 1 downto 0));
end component;

signal full_out: std_logic;
signal empty_out: std_logic;
signal WR_RAM: std_logic;
signal MACsel: std_logic;
signal incrementG: std_logic;
signal incrementP: std_logic;
signal RAM_ADDR: std_logic_vector(3 downto 0);

begin

RINGBUFFERCONTROL: RBC port map (
						 DAV => DAV,
						 CTS => CTS,
						 full => full_out,
						 empty => empty_out,
						 Wr => WR_RAM,
						 selPG => MACsel,
						 incPut => incrementP,
						 incGet => incrementG,
						 Wreg => Wreg,
						 DAC => DAC);
						 
MEMORYCONTROLADDRESS: MAC port map (
							 CLK => CLK,
							 RESET => RESET,
							 putget => MACsel,
							 incPut => incrementP,
							 incGet => incrementG,
							 A => RAM_ADDR,
							 full => full_out,
							 empty => empty_out);
							 
RINGBUFFERRAM: RBRAM port map (
	  CLK => CLK,
	  A => RAM_ADDR,
	  Din => D,
	  wr => WR_RAM,
	  Dout => Q);
	  
end arch_RB;
							 