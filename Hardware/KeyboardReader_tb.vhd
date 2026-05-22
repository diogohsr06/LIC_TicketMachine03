library ieee;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity KeyboardReader_tb is
end KeyboardReader_tb;

architecture behavior of KeyboardReader_tb is

    component KeyboardReader
        port(
            Rows  : in  std_logic_vector(3 downto 0);
            RESET : in  std_logic;
            Osc   : in  std_logic;
            TXclk : in  std_logic;
            TXd   : out std_logic;
            Cols  : out std_logic_vector(3 downto 0)
        );
    end component;

    -- Sinais de interface
    signal Rows  : std_logic_vector(3 downto 0) := "0000";
    signal RESET : std_logic := '0';
    signal Osc   : std_logic := '0';
    signal TXclk : std_logic := '0';
    signal TXd   : std_logic;
    signal Cols  : std_logic_vector(3 downto 0);

    -- Períodos de relógio
    constant Osc_period   : time := 20 ns;   -- 50 MHz (FPGA)
    constant TXclk_period : time := 400 ns;  -- Software (mais lento para ver bem os bits)

begin

    -- Instanciação do Sistema Completo
    uut: KeyboardReader port map (
        Rows  => Rows,
        RESET => RESET,
        Osc   => Osc,
        TXclk => TXclk,
        TXd   => TXd,
        Cols  => Cols
    );

    -- Gerador do relógio da FPGA
    Osc_process : process
    begin
        Osc <= '0';
        wait for Osc_period/2;
        Osc <= '1';
        wait for Osc_period/2;
    end process;

    -- Gerador do relógio série (PC/Kotlin)
    -- Na realidade o Kotlin é que gera isto, aqui simulamos uma oscilação contínua
    TXclk_process : process
    begin
        TXclk <= '0';
        wait for TXclk_period/2;
        TXclk <= '1';
        wait for TXclk_period/2;
    end process;

    -- Processo de Estímulo (Simular o utilizador)
    stim_proc: process
    begin		
        -- 1. Reset do sistema
        RESET <= '1';
        wait for 100 ns;
        RESET <= '0';
        wait for 200 ns;

        -----------------------------------------------------------
        -- SIMULAR PRESSÃO DA TECLA '5'
        -----------------------------------------------------------
        -- A tecla '5' costuma estar na Coluna 1, Linha 1.
        -- Temos de esperar que o KeyDecode ative a Coluna 1 para puxar a Linha 1.
        report "A aguardar varrimento de Colunas para premir tecla...";
        
        -- Esperamos 100 ciclos de relógio para garantir que o scanner passou por lá
        for i in 1 to 100 loop
            -- Se a Coluna 1 estiver ativa (assumindo lógica positiva "0010")
            -- NOTA: Se o teu KeyDecode usar lógica negativa, altera para "1101"
            if Cols = "0010" then 
                Rows <= "0010"; -- Prime a Linha 1
            else
                Rows <= "0000"; -- Nenhuma tecla nas outras colunas
            end if;
            wait for Osc_period;
        end loop;

        Rows <= "0000"; -- Soltou a tecla
        
        -----------------------------------------------------------
        -- OBSERVAÇÃO
        -----------------------------------------------------------
        -- Neste ponto, a tecla deve ter entrado no RingBuffer.
        -- Como o KeyTransmitter está livre (KBfree = '1'), ele deve 
        -- começar a transmitir no TXd imediatamente.
        
        wait for 10 us; -- Tempo para ver a trama série completa sair

        report "Simulação do KeyboardReader concluída!";
        wait;
    end process;

end behavior;