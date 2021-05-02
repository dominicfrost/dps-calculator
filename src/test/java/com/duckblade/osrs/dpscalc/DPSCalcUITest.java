package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.calc.CalcManager;
import com.duckblade.osrs.dpscalc.calc.MageDpsCalc;
import com.duckblade.osrs.dpscalc.calc.MeleeDpsCalc;
import com.duckblade.osrs.dpscalc.calc.RangedDpsCalc;
import com.duckblade.osrs.dpscalc.ui.DpsCalculatorPanel;
import com.duckblade.osrs.dpscalc.ui.equip.EquipmentPanel;
import com.duckblade.osrs.dpscalc.ui.npc.NpcStatsPanel;
import com.duckblade.osrs.dpscalc.ui.skills.SkillsPanel;
import java.awt.Cursor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import net.runelite.client.ui.ContainableFrame;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.skin.SubstanceRuneLiteLookAndFeel;
import net.runelite.client.util.SwingUtil;

public class DPSCalcUITest
{

	public static void main(String[] args) throws InterruptedException, InvocationTargetException
	{
		SwingUtilities.invokeAndWait(() ->
		{
			// roughly copied from RuneLite's ClientUI.java init()
			SwingUtil.setupDefaults();
			SwingUtil.setTheme(new SubstanceRuneLiteLookAndFeel());
			SwingUtil.setFont(FontManager.getRunescapeFont());

			ContainableFrame frame = new ContainableFrame();
			frame.getLayeredPane().setCursor(Cursor.getDefaultCursor());
			frame.add(new DpsCalculatorPanel(
					null,
					new CalcManager(new MageDpsCalc(), new MeleeDpsCalc(), new RangedDpsCalc()),
					new NpcStatsPanel(new NpcDataManager()),
					new EquipmentPanel(null, null, new ItemDataManager()),
					new SkillsPanel(null)
			));

			frame.setSize(242, 800);
			frame.setResizable(true);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}

}
