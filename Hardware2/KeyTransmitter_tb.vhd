library ieee;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity KeyTransmitter_tb is
end KeyTransmitter_tb;

architecture behavior of KeyTransmitter_tb is

    -- Componente a testar (UUT - Unit Under Test)
    component KeyTransmitter
        port(
            Osc    : in  std_logic;
            Load   : in  std_logic;
            D      : in  std_logic_vector(3 downto 0);
            TXclk  : in  std_logic;
            RESET  : in  std_logic;
            KBfree : out std_logic;
            TXd    : out std_logic
        );
    end component;

    -- Sinais de entrada
    signal Osc    : std_logic := '0';
    signal Load   : std_logic := '0';
    signal D      : std_logic_vector(3 downto 0) := (others => '0');
    signal TXclk  : std_logic := '0';
    signal RESET  : std_logic := '0';

    -- Sinais de saída
    signal KBfree : std_logic;
    signal TXd    : std_logic;

    -- Definição de períodos de relógio
    constant Osc_period   : time := 20 ns;  -- 50 MHz
    constant TXclk_period : time := 200 ns; -- Muito mais lento que o Osc

begin

    -- Instanciação da UUT
    uut: KeyTransmitter port map (
        Osc    => Osc,
        Load   => Load,
        D      => D,
        TXclk  => TXclk,
        RESET  => RESET,
        KBfree => KBfree,
        TXd    => TXd
    );

    -- Processo do relógio Osc (FPGA)
    Osc_process : process
    begin
        Osc <= '0';
        wait for Osc_period/2;
        Osc <= '1';
        wait for Osc_period/2;
    end process;

    -- Processo do relógio TXclk (PC/Software)
    TXclk_process : process
    begin
        TXclk <= '0';
        wait for TXclk_period/2;
        TXclk <= '1';
        wait for TXclk_period/2;
    end process;

    -- Processo de Estímulo
    stim_proc: process
    begin		
        -- 1. Reset do Sistema
        RESET <= '1';
        wait for 100 ns;
        RESET <= '0';
        wait for 50 ns;

        -- 2. Preparar dados (Exemplo: Tecla 'A' -> 1010)
        D <= "1010";
        wait until falling_edge(Osc);
        
        -- 3. Gerar pulso de Load (Wreg do Ring Buffer)
        Load <= '1';
        wait for Osc_period;
        Load <= '0';

        -- 4. Observar a transmissão
        -- Devem ser transmitidos 6 bits: 
        -- Start(0), D0(0), D1(1), D2(0), D3(1), Stop(1)
        
        -- Aguardar que o KBfree volte a '1' (fim da transmissão)
        wait until KBfree = '1';
        wait for 200 ns;

        -- 5. Testar outra tecla (Exemplo: 1111)
        D <= "1111";
        wait until falling_edge(Osc);
        Load <= '1';
        wait for Osc_period;
        Load <= '0';

        wait until KBfree = '1';
        wait for 500 ns;

        -- Terminar simulação
        report "Simulação concluída com sucesso!";
        wait;
    end process;

end behavior;