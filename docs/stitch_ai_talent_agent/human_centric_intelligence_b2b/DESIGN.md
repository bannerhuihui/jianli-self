---
name: Human-Centric Intelligence B2B
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#434655'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0053db'
  primary: '#004ac6'
  on-primary: '#ffffff'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#b4c5ff'
  secondary: '#505f76'
  on-secondary: '#ffffff'
  secondary-container: '#d0e1fb'
  on-secondary-container: '#54647a'
  tertiary: '#005a82'
  on-tertiary: '#ffffff'
  tertiary-container: '#0074a6'
  on-tertiary-container: '#e4f2ff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#d3e4fe'
  secondary-fixed-dim: '#b7c8e1'
  on-secondary-fixed: '#0b1c30'
  on-secondary-fixed-variant: '#38485d'
  tertiary-fixed: '#c9e6ff'
  tertiary-fixed-dim: '#89ceff'
  on-tertiary-fixed: '#001e2f'
  on-tertiary-fixed-variant: '#004c6e'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  headline-lg:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 30px
    fontWeight: '600'
    lineHeight: '1.2'
  headline-lg-mobile:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.2'
  headline-md:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
  headline-sm:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 20px
    fontWeight: '600'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.6'
  body-sm:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 12px
    fontWeight: '400'
    lineHeight: '1.6'
  label-md:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 14px
    fontWeight: '500'
    lineHeight: '1'
  label-sm:
    fontFamily: Noto Sans SC, Inter, sans-serif
    fontSize: 12px
    fontWeight: '500'
    lineHeight: '1'
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  2xl: 48px
  gutter: 24px
  margin: 24px
---

## Brand & Style

This design system is built for the high-stakes environment of Chinese B2B SaaS, where efficiency, clarity, and intelligence are paramount. The brand personality is **Professional, Reliable, and Forward-Thinking**, focusing on "Human-Centric Intelligence"—the idea that complex data should be served with extreme clarity to empower human decision-making.

The aesthetic follows a **Corporate / Modern** style. It prioritizes high information density without sacrificing visual breathing room. The interface feels systematic and structured, utilizing a logic-driven layout that minimizes cognitive load for users navigating complex workflows. It is designed to look authoritative in a boardroom while remaining highly functional for daily power users.

## Colors

The color palette is anchored by a vibrant yet professional **Primary Blue (#2563eb)**, a color synonymous with trust and technology in the enterprise sector. 

- **Primary:** Used for main actions, active states, and brand highlights.
- **Secondary:** A neutral Slate used for secondary actions and subtle UI elements to prevent visual fatigue.
- **Neutrals:** A range of cool greys (Zinc/Slate) provides the foundation for the UI, ensuring that content and data visualizations stand out.
- **Functional Colors:** Standardized Success (Green), Warning (Amber), and Error (Red) tones are calibrated for high legibility against the neutral backgrounds to ensure critical system statuses are never missed.

The default mode is **Light**, providing a clean, paper-like clarity preferred in professional Chinese business environments, though the tokens are structured to support a future dark mode transition.

## Typography

Typography is optimized for **Simplified Chinese (SC)** combined with **Latin characters**. We utilize `notoSans` (Noto Sans SC) as the primary typeface for its exceptional legibility across thousands of glyphs and various weights.

- **Line Height:** Chinese characters require slightly more vertical breathing room than Latin text; a base line height of `1.6` is applied to body text to ensure readability in dense data contexts.
- **Hierarchy:** We use semi-bold weights (`600`) for headings to create a clear visual anchor on the page.
- **System Fallbacks:** For performance in Chinese B2B contexts, the CSS stack should prioritize `PingFang SC`, `Microsoft YaHei`, and `Hiragino Sans GB` to ensure a native look and feel across Windows and macOS.

## Layout & Spacing

This design system employs a **12-column fluid grid** for desktop and wide screens, ensuring the UI utilizes the expansive real estate common in B2B dashboards. 

- **The 4px Rule:** All spacing and layout dimensions are increments of 4px, creating a rhythmic and predictable visual structure.
- **Margins & Gutters:** A standard 24px gutter and margin are used on desktop to provide a professional "wide-screen" feel. On mobile, gutters and margins reduce to 16px.
- **Density:** To accommodate the high-information needs of Chinese SaaS users, vertical spacing between form elements and list items is kept compact (16px), while larger sections are separated by 32px or 48px to clearly demarcate the information architecture.

## Elevation & Depth

Visual hierarchy is established through a combination of **Tonal Layers** and **Ambient Shadows**.

1.  **Background (Level 0):** The base layer uses the primary neutral hex (#f8fafc).
2.  **Surface (Level 1):** Cards, white-space containers, and sidebars use a pure white background.
3.  **Elevation (Level 2):** Applied to hover states or active elements. It uses a very soft, diffused shadow: `0 4px 12px rgba(0, 0, 0, 0.05)`.
4.  **Overlay (Level 3):** Used for modals, dropdowns, and pickers. These use a more pronounced shadow to separate them from the workspace: `0 12px 32px rgba(0, 0, 0, 0.1)`.

Low-contrast outlines (1px solid #e2e8f0) are used globally to define container boundaries, ensuring structural integrity even on low-quality office monitors.

## Shapes

The shape language reflects the **ROUND_FOUR** logic (8px base radius). This specific level of roundedness (Level 2) strikes a balance between the precision of professional software and the approachability of modern human-centric design.

- **Standard Components:** Buttons, Input fields, and Cards utilize the 8px (0.5rem) radius.
- **Large Components:** Modals and large containers use the `rounded-lg` 16px (1rem) radius.
- **Small Components:** Tags, chips, and checkboxes use a smaller 4px (0.25rem) radius to maintain crispness at small scales.

## Components

### Buttons
Primary buttons use the Primary Blue (#2563eb) with white text and a subtle 8px radius. Secondary buttons use a light slate ghost-style outline. Button height is standardized at 32px (small), 40px (default), and 48px (large) to fit various layout densities.

### Input Fields
Inputs feature a 1px border (#cbd5e1) and an 8px radius. On focus, the border changes to Primary Blue with a subtle 3px outer glow (ring) of the same color at 10% opacity.

### Data Tables
Tables are the heart of B2B SaaS. They use a high-density approach with 12px or 14px text. Headers are styled with a light neutral background (#f1f5f9) and a bold bottom border. Row hovering is mandatory for tracking data across columns.

### Chips & Tags
Tags are used for status and categorization. They utilize a soft background (10% opacity of the status color) with high-contrast text for maximum legibility.

### Cards
Cards are used to group related information. They have a white background, an 8px radius, and a 1px border. Shadows are only applied when cards are interactive or as a "lift" effect during drag-and-drop.

### Navigation
The sidebar navigation uses a dark slate or white background with high-contrast icons. Active states are indicated by a 4px vertical bar on the left in Primary Blue and a subtle background tint for the entire menu item.