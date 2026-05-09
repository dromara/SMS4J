# Security Policy

Thanks for helping keep SMS4J and the wider [dromara](https://github.com/dromara) ecosystem secure.

## Reporting a vulnerability

**Please do not open a public issue, discussion, or pull request that describes a suspected vulnerability** — that gives attackers a head-start before a fix lands.

Use one of these private channels instead:

1. **GitHub's Private Vulnerability Reporting (preferred).**
   Open a private security advisory at <https://github.com/dromara/SMS4J/security/advisories/new>.
   GitHub automatically routes it to the maintainers, keeps the discussion private until you and the maintainers agree to publish, and (optionally) assigns a CVE on publish.

2. **Email.**
   If GitHub PVR is unavailable to you for any reason, email the project lead directly. Maintainers should add the preferred email here when they configure the policy.

## What to include

A good report contains:

- A clear description of the vulnerability and its impact.
- Steps to reproduce against a specific commit SHA or release tag.
- Affected code locations (`file:line`) where useful.
- A suggested fix or mitigation if you have one.
- Whether you'd like credit in the published advisory and, if so, under what name.

## Scope and supported versions

SMS4J is a unified Java SMS-sending abstraction with adapters for major Chinese and global SMS providers (Aliyun, Tencent, Huawei, Twilio, etc.) plus rate-limit and template-management helpers. The maintainers support security fixes on the latest minor release. Older releases will not generally receive backports.

In-scope vulnerability classes include:

- Authentication / authorization bypass on the admin / management surface
- SSRF via webhook / callback / signed-URL handling in adapters
- Cryptographic misuse in stored credentials / signing keys
- Insecure deserialization in adapter response parsing
- Template / parameter injection that enables provider-side abuse
- Rate-limit bypass that enables credential-stuffing / SMS pumping

Out-of-scope:

- Findings against intentionally trusted-admin features (config-driven behavior that the framework explicitly delegates to the integrator).
- Issues that require an attacker to already have full SMS-provider account access.
- Best-practice complaints without a concrete impact (e.g. "this header should be set", "TLS version should be raised").

## Process

After you submit:

1. A maintainer will acknowledge receipt within roughly 1 week.
2. We'll triage the report: confirm severity, scope, and reproducibility.
3. We'll work with you on a fix and a coordinated disclosure timeline (typically up to 90 days, longer if the fix is structural).
4. On publication, we credit you in the advisory unless you ask not to be credited.

## Hall of fame

Reporters who have helped harden SMS4J via responsible disclosure will be listed here once the corresponding advisory is published.

---

This policy is suggested via [GitHub's "Suggest a security policy" workflow](https://docs.github.com/en/code-security/getting-started/adding-a-security-policy-to-your-repository). Maintainers can edit any section freely; the most important thing is that **a private reporting channel exists** so researchers can submit findings responsibly.
