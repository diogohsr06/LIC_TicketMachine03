LIBRARY ieee;
USE ieee.STD_LOGIC_1164.ALL;

ENTITY MUX6 IS
    PORT (
		D : in STD_LOGIC_VECTOR(7 downto 0);
		S : in STD_LOGIC_VECTOR(2 downto 0);
		Q: out STD_LOGIC
    );
END MUX6;

ARCHITECTURE arch_MUX6 OF MUX6 IS
BEGIN
    process(D, S)
			begin
				case S is
					when "000" => Q <= D(0);
					when "001" => Q <= D(1);
					when "010" => Q <= D(2);
					when "011" => Q <= D(3);
					when "100" => Q <= D(4);
					when "101" => Q <= D(5);
					when "110" => Q <= D(6);
					when "111" => Q <= D(7);
					when others => Q <= '1'; -- 0
				end case;
			end process;
END arch_MUX6;