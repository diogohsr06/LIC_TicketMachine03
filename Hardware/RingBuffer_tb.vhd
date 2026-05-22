library ieee;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity RingBuffer_tb is
end RingBuffer_tb;

architecture behavior of RingBuffer_tb is

    -- Componente a testar
    component RingBuffer
        port(
            CLK   : in  std_logic;
            RESET : in  std_logic;
            CTS   : in  std_logic;
            DAV   : in  std_logic;
            D     : in  std_logic_vector(3 downto 0);
            Wreg  : out std_logic;
            Q     : out std_logic_vector(3 downto 0);
            DAC   : out std_logic
        );
    end component;

    -- Sinais de interface
    signal CLK   : std_logic := '0';
    signal RESET : std_logic := '0';
    signal CTS   : std_logic := '0';
    signal DAV   : std_logic := '0';
    signal D     : std_logic_vector(3 downto 0) := (others => '0');
    signal Wreg  : std_logic;
    signal Q     : std_logic_vector(3 downto 0);
    signal DAC   : std_logic;

    -- Período do Clock (50MHz)
    constant CLK_period : time := 20 ns;

begin

    -- Instanciação do RingBuffer
    uut: RingBuffer port map (
        CLK   => CLK,
        RESET => RESET,
        CTS   => CTS,
        DAV   => DAV,
        D     => D,
        Wreg  => Wreg,
        Q     => Q,
        DAC   => DAC
    );

    -- Geração do Clock
    CLK_process : process
    begin
        CLK <= '0';
        wait for CLK_period/2;
        CLK <= '1';
        wait for CLK_period/2;
    end process;

    -- Processo de Estímulo
    stim_proc: process
    begin		
        -- 1. Reset inicial
        RESET <= '1';
        wait for 50 ns;
        RESET <= '0';
        wait for 50 ns;

        -----------------------------------------------------------
        -- TESTE 1: Escrita de 3 valores (Simular KeyDecode)
        -----------------------------------------------------------
        -- Tecla 1 (Valor "0001")
        D <= "0001";
        DAV <= '1';
        wait until DAC = '1'; -- Espera o RingBuffer aceitar (Acknowledge)
        DAV <= '0';
        wait for CLK_period * 2;

        -- Tecla 2 (Valor "0010")
        D <= "0010";
        DAV <= '1';
        wait until DAC = '1';
        DAV <= '0';
        wait for CLK_period * 2;

        -- Tecla 3 (Valor "0011")
        D <= "0011";
        DAV <= '1';
        wait until DAC = '1';
        DAV <= '0';
        wait for CLK_period * 5;

        -----------------------------------------------------------
        -- TESTE 2: Leitura de 2 valores (Simular KeyTransmitter)
        -----------------------------------------------------------
        -- O Transmitter diz que está livre (CTS = 1)
        CTS <= '1';
        wait until Wreg = '1'; -- Espera o sinal de Load para o transmissor
        -- Simulamos que o transmissor ficou ocupado a enviar o primeiro bit
        CTS <= '0'; 
        wait for CLK_period * 10; -- Tempo de simulação de envio série
        
        -- Transmitter fica livre outra vez
        CTS <= '1';
        wait until Wreg = '1';
        CTS <= '0';
        wait for CLK_period * 5;

        -----------------------------------------------------------
        -- TESTE 3: Encher o buffer (Capacidade é 16, pois MAC é 4 bits)
        -----------------------------------------------------------
        report "A encher o buffer...";
        for i in 4 to 18 loop
            D <= std_logic_vector(to_unsigned(i, 4));
            DAV <= '1';
            -- Se o buffer encher, o DAC pode demorar ou não acontecer
            wait for CLK_period * 4; 
            DAV <= '0';
            wait for CLK_period;
        end loop;

        wait for 200 ns;
        report "Simulação do RingBuffer terminada!";
        wait;
    end process;

end behavior;