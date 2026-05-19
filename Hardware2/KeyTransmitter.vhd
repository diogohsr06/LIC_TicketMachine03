library ieee;
use IEEE.std_logic_1164.all;

entity KeyTransmitter is
    port(
        Osc    : in  std_logic;                     -- relógio principal da FPGA
        Load   : in  std_logic;                     -- Wreg do Ring Buffer
        D      : in  std_logic_vector(3 downto 0); -- dados do Ring Buffer
        TXclk  : in  std_logic;                     -- relógio série do Control (PC)
        RESET  : in  std_logic;
        KBfree : out std_logic;
        TXd    : out std_logic
    );
end KeyTransmitter;

architecture arch_KT of KeyTransmitter is

    component KTC is
        port(
            CLK      : in  std_logic;
            RESET    : in  std_logic;
            Load     : in  std_logic;
            cntDone  : in  std_logic;
            KBfree   : out std_logic;
            Ereg     : out std_logic;
            CE       : out std_logic;
            cntReset : out std_logic
        );
    end component;

    component Counter3 is
        port(
            CE    : in  std_logic;
            CLK   : in  std_logic;
            RESET : in  std_logic;
            Q     : out std_logic_vector(2 downto 0)
        );
    end component;

    component MUX6 is
        port(
            X : in  std_logic_vector(5 downto 0);
            S : in  std_logic_vector(2 downto 0);
            R : out std_logic
        );
    end component;

    signal Q_out      : std_logic_vector(3 downto 0);
    signal counterOut : std_logic_vector(2 downto 0);
    signal CE_s       : std_logic;
    signal Ereg_s     : std_logic;
    signal cntReset_s : std_logic;
    signal cntDone_s  : std_logic;
    signal mux_out    : std_logic;

begin

    cntDone_s <= '1' when counterOut = "101" else '0';

    -- Registo de dados: síncrono com Osc, enable = Ereg (sem clock gating)
    process(Osc, RESET)
    begin
        if RESET = '1' then
            Q_out <= (others => '0');
        elsif rising_edge(Osc) then
            if Ereg_s = '1' then
                Q_out <= D;
            end if;
        end if;
    end process;

    UCTRL : KTC port map (
        CLK      => Osc,
        RESET    => RESET,
        Load     => Load,
        cntDone  => cntDone_s,
        KBfree   => KBfree,
        Ereg     => Ereg_s,
        CE       => CE_s,
        cntReset => cntReset_s
    );

    UCOUNTER : Counter3 port map (
        CE    => CE_s,
        CLK   => TXclk,
        RESET => cntReset_s,
        Q     => counterOut
    );

    UMUX : MUX6 port map (
        X(0) => '0',        -- start bit (TXd desce → sinaliza Control)
        X(1) => Q_out(0),   -- K0
        X(2) => Q_out(1),   -- K1
        X(3) => Q_out(2),   -- K2
        X(4) => Q_out(3),   -- K3
        X(5) => '1',        -- stop bit
        S    => counterOut,
        R    => mux_out
    );

    -- Linha em repouso ('1') fora de SEND; segue MUX durante SEND
    TXd <= mux_out when CE_s = '1' else '1';

end arch_KT;