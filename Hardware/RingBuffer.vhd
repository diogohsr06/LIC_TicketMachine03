---------------------------------------------------------------------------------------------
-- Ring Buffer
---------------------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer is
    port(
		  --Inputs
        CLK: in  std_logic;								--Relogio da FPGA
        RESET: in  std_logic;								--Reinicia o sistema
        DAV: in  std_logic;								--Data available (Existe informaçao a armazenar)
        CTS: in  std_logic;								--Clear to send (Pronto para ler)
        Din: in  std_logic_vector(3 downto 0);		--Data in

		  --Outputs
        DAC: out std_logic;								--Data accepted (Dados aceites)
        Wreg: out std_logic;								--Enable do registo (Load)
        Dout: out std_logic_vector(3 downto 0)		--Data out
);
end RingBuffer;

architecture arch_RB of RingBuffer is
--Maquina de estados do Ring Buffer
component RBC is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        DAV: in std_logic;
        CTS: in std_logic;
        full: in std_logic;
        empty: in std_logic;
        Wr: out std_logic;
        PnG: out std_logic;
        incPut: out std_logic;
        incGet: out std_logic;
        Wreg: out std_logic;
        DAC: out std_logic);
end component;
--Memory Address Control
component MAC is
    port(
        CLK: in  std_logic;
        RESET: in  std_logic;
        putget: in  std_logic;
        incPut: in  std_logic;
        incGet: in  std_logic;
        S: out std_logic_vector(3 downto 0);
        full: out std_logic;
        empty: out std_logic);
end component;
--Memoria
component RBRAM is
    generic(
        ADDRESS_WIDTH: natural := 4;
        DATA_WIDTH: natural := 4);
    port(
         A: in  std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
         wr: in  std_logic;
         din: in  std_logic_vector(DATA_WIDTH - 1 downto 0);
         dout: out std_logic_vector(DATA_WIDTH - 1 downto 0));
end component;

--Sinais intermediarios
signal Wr_s     : std_logic;
signal PnG_s  : std_logic;
signal incPut_s : std_logic;
signal incGet_s : std_logic;
signal Wreg_s   : std_logic;
signal A_s      : std_logic_vector(3 downto 0);
signal full_s   : std_logic;
signal empty_s  : std_logic;
signal Dout_ram : std_logic_vector(3 downto 0);

begin

URBC: RBC port map(																			--Instanciaçao da maquina de estados
      CLK => CLK,
      RESET => RESET,
      DAV => DAV,
      CTS => CTS,
      full => full_s,
      empty => empty_s,
      Wr => Wr_s,
      PnG => PnG_s,
      incPut => incPut_s,
      incGet => incGet_s,
      Wreg => Wreg_s,
      DAC => DAC);

UMAC: MAC port map(																			--Instanciaçao da MAC
      CLK => CLK,
      RESET => RESET,
      putget => PnG_s,
      incPut => incPut_s,
      incGet => incGet_s,
      S => A_s,
      full => full_s,
      empty => empty_s);

MEMORY: RBRAM generic map(ADDRESS_WIDTH => 4, DATA_WIDTH => 4) port map(	--Instanciaçao da RAM
        A => A_s,
        wr => Wr_s,
        din => Din,
        dout => Dout_ram);

Dout <= Dout_ram;
Wreg <= Wreg_s;

end arch_RB;