---
name: Human-Centric Intelligence
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#434655'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0053db'
  primary: '#004ac6'
  on-primary: '#ffffff'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#b4c5ff'
  secondary: '#565e74'
  on-secondary: '#ffffff'
  secondary-container: '#dae2fd'
  on-secondary-container: '#5c647a'
  tertiary: '#006242'
  on-tertiary: '#ffffff'
  tertiary-container: '#007d55'
  on-tertiary-container: '#bdffdb'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#dae2fd'
  secondary-fixed-dim: '#bec6e0'
  on-secondary-fixed: '#131b2e'
  on-secondary-fixed-variant: '#3f465c'
  tertiary-fixed: '#6ffbbe'
  tertiary-fixed-dim: '#4edea3'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005236'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  display-lg:
    fontFamily: Hanken Grotesk
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  display-md:
    fontFamily: Hanken Grotesk
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 38px
    letterSpacing: -0.01em
  headline-lg:
    fontFamily: Hanken Grotesk
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-lg-mobile:
    fontFamily: Hanken Grotesk
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  headline-md:
    fontFamily: Hanken Grotesk
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Hanken Grotesk
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Hanken Grotesk
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Hanken Grotesk
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Hanken Grotesk
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 16px
  label-sm:
    fontFamily: Hanken Grotesk
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  code:
    fontFamily: Courier Prime
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 8px
  container-max: 1440px
  gutter: 24px
  margin-desktop: 40px
  margin-tablet: 24px
  margin-mobile: 16px
  stack-sm: 4px
  stack-md: 12px
  stack-lg: 24px
---

## Brand & Style
The design system is anchored in the concept of **Human-Centric Intelligence**. It moves away from the cold, "black-box" aesthetics often associated with AI, instead focusing on transparency, evidence-based decision-making, and professional reliability. The style is **Modern Corporate SaaS**: it prioritizes information density and clarity without sacrificing the "breathable" quality of a premium tool.

The emotional response should be one of **calm confidence**. Recruiters and hiring managers should feel that the AI is a sophisticated assistant rather than a replacement. The visual language utilizes a "Low-Contrast Professional" approach, where subtle borders and soft tonal shifts define hierarchy, ensuring that high-density data visualizations remain legible and non-intimidating.

## Colors
The palette is dominated by **Professional Blues**, which evoke trust and stability. 
- **Primary Blue (#2563EB):** Used for primary actions, active states, and brand-critical touchpoints.
- **Deep Slate (#0F172A):** Used for high-level navigation and primary headings to provide a grounded, authoritative feel.
- **Soft Slate/Grays:** The foundation for the UI's structure, using #F8FAFC for large background areas to reduce eye strain.
- **Functional Accents:** Emerald is used strictly for high-confidence AI scores and successful matches, while Amber flags potential risks or "areas for investigation" in a talent profile.

## Typography
This design system utilizes **Hanken Grotesk** for its precise, contemporary geometry which performs exceptionally well in high-density SaaS environments. 

**Simplified Chinese Implementation:**
When localized, the system must fallback to **PingFang SC** (macOS/iOS) and **Microsoft YaHei** (Windows). The typeface weight mapping should remain consistent (Regular for body, Medium/Semibold for headings).

**Hierarchy Rules:**
- **Display & Headlines:** Use Hanken Grotesk with tighter letter spacing and Semibold/Bold weights to anchor data sections.
- **Body Text:** Maintained at 16px for optimal readability of talent descriptions and AI summaries.
- **Labels:** Small, uppercase-neutral labels (12px) are used for metadata like "Years of Experience" or "Current Role" to maintain a structured, categorical look.

## Layout & Spacing
The layout follows a **Fixed-Fluid Hybrid Grid**. Content is housed in a 12-column centered container (max 1440px) to ensure dashboards don't become overly stretched on ultra-wide monitors, preserving the "Human-Centered" scale.

**Spacing Principles:**
- **The 8px Rhythm:** All padding, margins, and component heights follow an 8px base unit. 
- **Information Density:** Talent cards use tighter internal spacing (16px) to allow more profile data to be visible above the fold, while external container margins remain generous (40px) to prevent visual clutter.
- **Breakpoints:**
  - **Desktop (1024px+):** 12 columns, 24px gutters. Full sidebar navigation.
  - **Tablet (768px - 1023px):** 8 columns, 16px gutters. Condensed sidebar.
  - **Mobile (Below 768px):** 4 columns, 16px gutters. Top-bar navigation with bottom-sheet drawers for evidence details.

## Elevation & Depth
Depth in this design system is primarily created through **Tonal Layering** rather than heavy shadows. This maintains the "Structured" and "Clean" feel required for B2B tools.

- **Level 0 (Background):** #F8FAFC. The canvas.
- **Level 1 (Cards/Containers):** #FFFFFF with a 1px border (#E2E8F0). This is the default state for talent profiles and list items.
- **Level 2 (Active/Hover):** A very soft, diffused shadow (0px 4px 12px rgba(15, 23, 42, 0.05)) is applied to cards when hovered or selected, signaling interactivity.
- **Level 3 (Modals/Evidence Drawers):** High-z-index surfaces that use a 15% backdrop blur (glassmorphism) behind them to keep the user grounded in their current recruitment context while focusing on specific talent evidence.

## Shapes
We use a **Soft (0.25rem / 4px)** roundedness level. This choice reflects the "Professional" and "Precise" nature of recruitment data. 

- **Components:** Buttons, Input fields, and Chips all utilize the 4px radius.
- **Large Containers:** Talent cards and Radar Chart backgrounds use `rounded-lg` (8px) to provide a slightly friendlier, modern SaaS appearance.
- **Data Visuals:** Radar charts and progress bars should use rounded caps to maintain the "Human-Centric" softness within a technical framework.

## Components

### Talent Profile Cards
The core unit of the system. Cards feature a subtle 1px border (#E2E8F0). The header includes the talent's name and a **Confidence Badge** (a pill-shaped chip with a light green background and dark green text). 

### Evidence Drawers
When an AI-generated insight is clicked, a slide-out drawer appears from the right. This drawer must contain "raw data" evidence (e.g., specific resume snippets or GitHub commits) to build trust. It uses a high-contrast white surface against a dimmed background.

### Confidence Badges & Radar Charts
- **Badges:** Use a semantic background (Success Emerald at 10% opacity) with bold text of the same hue.
- **Radar Charts:** Drawn with a Primary Blue (#2563EB) fill at 20% opacity and a 2px solid stroke. Grid lines in the chart use #E2E8F0.

### Buttons & Actions
- **Primary:** Solid #2563EB with white text. 4px rounded corners.
- **Secondary (Export):** Ghost style with #64748B border and text. Often paired with an icon (e.g., "Download PDF").
- **AI-Action:** A special button variant using a subtle gradient from #2563EB to #3B82F6 to indicate AI-powered processing.

### Step Progress Indicators
Vertical or horizontal steppers used for recruitment pipelines. Completed steps use the Success Emerald icon; active steps use the Primary Blue pulse; upcoming steps are muted gray.