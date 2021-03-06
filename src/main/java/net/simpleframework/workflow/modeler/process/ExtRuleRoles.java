package net.simpleframework.workflow.modeler.process;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.Application;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

public class ExtRuleRoles {

	private static Map<String, Map<String, Object>> ALL_CLASS1 = new HashMap<>();

	private void init() {
		final ModelGraph modelGraph = une.getModelGraph();
		final String url = modelGraph.getTabbedContent().getTreeNode().getUrl();
		CLASS = ALL_CLASS1.get(url);
		if (null == CLASS) {
			try {
				final Map<String, Object> p = Application.remote().call(url, "participants",
						new KVMap());
				if (Application.isError(p)) {
					return;
				}
				ALL_CLASS1.put(url, p);
				CLASS = p;
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	UserNodeEditor une = null;
	Map<String, Object> CLASS = null;

	public ExtRuleRoles(final UserNodeEditor une) {
		this.une = une;
		init();
	}

	public List<?> getAllClass() {
		return (List<?>) CLASS.get("all-class");
	}

	@SuppressWarnings("rawtypes")
	public int getRuleRolesI(final String formClass) {
		int i = 0;
		final List<?> list = getAllClass();
		for (final Object obj : list) {
			if (obj instanceof Map) {
				final String rulerole = (String) ((Map) obj).get("name");
				i++;
				if (rulerole.equals(formClass)) {
					return i;
				}
			}
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public String getRuleRoleName(final String text) {
		final List<?> list = getAllClass();
		for (final Object obj : list) {
			if (obj instanceof Map) {
				final String rulerole = (String) ((Map) obj).get("text");
				if (rulerole.equals(text)) {
					return (String) ((Map) obj).get("name");
				}
			}
		}
		return text;
	}

	@SuppressWarnings("rawtypes")
	public List<String> getRuleRoles() {
		final List<String> l = new ArrayList<>();
		final List<?> list = getAllClass();
		for (final Object obj : list) {
			if (obj instanceof Map) {
				l.add((String) ((Map) obj).get("text"));
			}
		}
		return l;
	}

	public static Map<String, JPanel> components = new HashMap<>();
	public static Map<String, Component> pcomponents = new HashMap<>();

	public void show(final String text) {
		final Iterator<String> iter = components.keySet().iterator();
		while (iter.hasNext()) {
			final String key = iter.next();
			final JPanel p = components.get(key);
			p.setVisible(key.equals(text));
		}
	}

	@SuppressWarnings("rawtypes")
	public JPanel getUI() {
		final JPanel p2 = new JPanel(new BorderLayout());
		final List<Component> p2s = new ArrayList<>();
		final List<?> list = getAllClass();
		for (final Object obj : list) {
			if (obj instanceof Map) {
				final String cname = (String) ((Map) obj).get("name");
				final List<?> params = (List<?>) ((Map) obj).get("params");
				if (null != params) {
					final List<Component> p1s = new ArrayList<>();
					for (final Object obj2 : params) {
						if (obj2 instanceof Map) {
							final Map par = (Map) obj2;
							final String pname = (String) par.get("name");

							final Vector<String> ps = new Vector<>();
							final List<?> values = (List<?>) par.get("values");
							JPanel m1 = null;
							Component pc = null;
							if (null != values) {
								for (final Object obj3 : values) {
									if (obj3 instanceof Map) {
										final Map v = (Map) obj3;
										ps.add((String) v.get("text"));
									}
								}
								m1 = SwingUtils.createKV(new JLabel((String) par.get("text")),
										pc = new JComboBox(ps), 150, true);
								pc.setName(cname + ":" + pname);
								((JComboBox) pc).addItemListener(new ItemListener() {
									@Override
									public void itemStateChanged(final ItemEvent e) {
										if (e.getStateChange() != ItemEvent.SELECTED) {
											return;
										}
										final Object obj = e.getSource();
										if (obj instanceof JComboBox) {
											final String[] name = ((JComboBox) obj).getName().split(":");
											final String t = ((JComboBox) obj).getSelectedItem().toString();
											final String v = (String) getParValI(name[0], name[1], t, false);
											changeParValue(name[0], name[1], v);
										}
									}
								});
							} else {
								m1 = SwingUtils.createKV(new JLabel((String) par.get("text")),
										pc = new JTextField(), 150, true);
								pc.setName(cname + ":" + pname);
								((JTextField) pc).addKeyListener(new KeyListener() {

									@Override
									public void keyTyped(final KeyEvent e) {
									}

									@Override
									public void keyPressed(final KeyEvent e) {
									}

									@Override
									public void keyReleased(final KeyEvent e) {
										final Object obj = e.getSource();
										if (obj instanceof JTextField) {
											final String[] name = ((JTextField) obj).getName().split(":");
											final String v = ((JTextField) obj).getText();
											changeParValue(name[0], name[1], v);
										}
									}
								});
							}
							p1s.add(m1);

							pcomponents.put(cname + ":" + pname, pc);
						}
					}

					final JPanel p1 = new JPanel(new BorderLayout());
					p1.add(createVertical(p1s));
					p2s.add(p1);

					components.put(cname, p1);
				}
			}
		}
		p2.add(createVertical(p2s));
		return p2;
	}

	private void changeParValue(final String cname, String pname, String v) {
		String ps = une.getParamsTf().getText();
		if (null == ps) {
			ps = "";
		}
		if (null == v) {
			v = "";
		}
		ps = ";" + ps;
		pname = ";" + pname + "=";
		final int n = ps.indexOf(pname);
		if (n > -1) {
			ps = ps + ";";
			final String p1 = ps.substring(0, n);
			String p2 = ps.substring(n + pname.length(), ps.length());

			p2 = p2.substring(p2.indexOf(";"));
			ps = p1 + pname + v + p2;
		} else {
			ps += pname + v;
		}
		if (v.equals("")) {
			ps = ps.replaceAll(pname, "");
		}
		if (ps.startsWith(";")) {
			ps = ps.substring(1);
		}
		if (ps.startsWith(";")) {
			ps = ps.substring(1);
		}
		if (ps.endsWith(";")) {
			ps = ps.substring(0, ps.length() - 1);
		}
		une.getParamsTf().setText(ps);
	}

	public void initPComponentsValues(final String classs, final String vs) {
		final Iterator<String> iter = pcomponents.keySet().iterator();
		while (iter.hasNext()) {
			final Map<String, String> m = getValues(vs);
			final String key = iter.next();
			if (key.startsWith(classs + ":")) {
				final Component c = pcomponents.get(key);
				final String[] k = key.split(":");
				final String v = m.get(k[1]);
				if (c instanceof JTextField) {
					((JTextField) c).setText(null == v ? "" : v);
				} else if (c instanceof JComboBox) {
					((JComboBox) c).setSelectedIndex((Integer) getParValI(classs, k[1], v, true));
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Object getParValI(final String classs, final String parname, final String vv,
			final boolean i) {
		final List<?> list = getAllClass();
		for (final Object obj : list) {
			if (obj instanceof Map) {
				final String cname = (String) ((Map) obj).get("name");
				if (!cname.equals(classs)) {
					continue;
				}
				final List<?> params = (List<?>) ((Map) obj).get("params");
				if (null != params) {
					for (final Object obj2 : params) {
						if (obj2 instanceof Map) {
							final Map par = (Map) obj2;
							if (!parname.equals(par.get("name").toString())) {
								continue;
							}
							final List<?> values = (List<?>) par.get("values");
							int n = 0;
							if (null != values) {
								for (final Object obj3 : values) {
									if (obj3 instanceof Map) {
										final Map v = (Map) obj3;
										if (i) {
											if (((String) v.get("name")).equals(vv)) {
												return n;
											}
										} else {
											if (((String) v.get("text")).equals(vv)) {
												return v.get("name");
											}
										}
										n++;
									}
								}
							}
						}
					}

				}
			}
		}
		return 0;
	}

	private Map<String, String> getValues(final String vs) {
		final HashMap<String, String> m = new HashMap<>();
		if (StringUtils.hasText(vs)) {
			final String[] _v = vs.split(";");
			for (final String v : _v) {
				if (StringUtils.hasText(v)) {
					final String[] vv = v.split("=");
					if (vv.length > 1) {
						m.put(vv[0], vv[1]);
					} else {
						m.put(vv[0], "");
					}
				}
			}
		}
		return m;
	}

	public static JPanel createVertical(final List<Component> components) {
		final JPanel jp = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 5, 4, 5);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		for (final Component component : components) {
			if (gbc.gridy == components.size() - 1) {
				gbc.weighty = 1.0;
			}
			jp.add(component, gbc);
			gbc.gridy++;
		}
		return jp;
	}
}
